package com.company.complaintsystem.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.company.complaintsystem.dto.AdminComplaintListDto;
import com.company.complaintsystem.dto.AdminDashboardResponseDto;
import com.company.complaintsystem.dto.AssignComplaintRequestDto;
import com.company.complaintsystem.dto.ComplaintCreateRequestDto;
import com.company.complaintsystem.dto.ComplaintResponseDto;
import com.company.complaintsystem.dto.ComplaintTimelineDto;
import com.company.complaintsystem.dto.CustomerComplaintResponseDto;

import com.company.complaintsystem.dto.MyComplaintListDto;
import com.company.complaintsystem.dto.ReassignComplaintRequestDto;
import com.company.complaintsystem.entity.*;
import com.company.complaintsystem.exception.InvalidOperationException;
import com.company.complaintsystem.exception.ResourceNotFoundException;
import com.company.complaintsystem.repository.ComplaintHistoryRepository;
import com.company.complaintsystem.repository.ComplaintRepository;
import com.company.complaintsystem.repository.UserRepository;
import com.company.complaintsystem.security.CustomUserDetails;
import com.company.complaintsystem.security.JwtUtil;

import lombok.RequiredArgsConstructor;



@Service
@RequiredArgsConstructor
public class ComplaintServiceImpl implements ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final UserRepository userRepository;
    private final ComplaintHistoryRepository complaintHistoryRepository;

    //CREATE COMPLAINT (WITH SLA)
    
    @Override
    public ComplaintResponseDto createComplaint(ComplaintCreateRequestDto dto) {

        CustomUserDetails userDetails =
                (CustomUserDetails) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        User currentUser = userDetails.getUser();

        LocalDateTime createdAt = LocalDateTime.now();

        Complaint complaint = Complaint.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .category(dto.getCategory())
                .priority(dto.getPriority())
                .status(ComplaintStatus.NEW)
                .createdBy(currentUser)
                .createdAt(createdAt)
                .slaDeadline(calculateSlaDeadline(createdAt, dto.getPriority()))
                .slaBreached(false)
                .build();

        Complaint saved = complaintRepository.save(complaint);
        saveHistory(
                saved,
                null,
                ComplaintStatus.NEW,
                currentUser
        );  
        
        return ComplaintResponseDto.builder()
                .id(saved.getId())
                .title(saved.getTitle())
                .description(saved.getDescription())
                .status(saved.getStatus())
                .build();
    }

   
    //  ASSIGN COMPLAINT
   
    @Override
    public ComplaintResponseDto assignComplaint(
            Long complaintId,
            AssignComplaintRequestDto dto) {

        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Complaint not found"));

        if (complaint.getStatus() != ComplaintStatus.NEW) {
            throw new InvalidOperationException(
                    "Only NEW complaints can be assigned");
        }

        User engineer = userRepository.findById(dto.getEngineerId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Engineer not found"));

        if (engineer.getRole() != Role.ENGINEER) {
            throw new InvalidOperationException(
                    "Assigned user must have ENGINEER role");
        }
        ComplaintStatus oldStatus = complaint.getStatus();
        complaint.setAssignedTo(engineer);
        complaint.setStatus(ComplaintStatus.ASSIGNED);

        Complaint saved = complaintRepository.save(complaint);
        saveHistory(complaint, oldStatus, ComplaintStatus.ASSIGNED, getCurrentUser());

        return ComplaintResponseDto.builder()
                .id(saved.getId())
                .title(saved.getTitle())
                .description(saved.getDescription())
                .status(saved.getStatus())
                .build();
    }

 
    // START WORK
    
    @Override
    public void startComplaint(Long complaintId) {

        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Complaint not found"));

        if (complaint.getStatus() != ComplaintStatus.ASSIGNED) {
            throw new InvalidOperationException(
                    "Only ASSIGNED complaints can be started");
        }

        User currentUser = getCurrentUser();

        if (!complaint.getAssignedTo().getId().equals(currentUser.getId())) {
            throw new InvalidOperationException(
                    "You are not assigned to this complaint");
        }
        ComplaintStatus oldStatus = complaint.getStatus();
        complaint.setStatus(ComplaintStatus.IN_PROGRESS);
        complaintRepository.save(complaint);
        saveHistory(complaint, oldStatus, ComplaintStatus.IN_PROGRESS, currentUser);
    }

   
    // RESOLVE
  
    @Override
    public void resolveComplaint(Long complaintId) {

        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Complaint not found"));

        if (complaint.getStatus() != ComplaintStatus.IN_PROGRESS) {
            throw new InvalidOperationException(
                    "Only IN_PROGRESS complaints can be resolved");
        }

        User currentUser = getCurrentUser();

        if (!complaint.getAssignedTo().getId().equals(currentUser.getId())) {
            throw new InvalidOperationException(
                    "You are not assigned to this complaint");
        }
        ComplaintStatus oldStatus = complaint.getStatus();
        complaint.setStatus(ComplaintStatus.RESOLVED);
        complaint.setResolvedAt(LocalDateTime.now());

        complaintRepository.save(complaint);
        saveHistory(complaint, oldStatus, ComplaintStatus.RESOLVED, currentUser);
    }
    
    
    // ADMIN 
    @Override
    public AdminDashboardResponseDto getAdminDashboard() {

        return AdminDashboardResponseDto.builder()
                .totalComplaints(complaintRepository.count())

                .newComplaints(
                        complaintRepository.countByStatus(ComplaintStatus.NEW))
                .assignedComplaints(
                        complaintRepository.countByStatus(ComplaintStatus.ASSIGNED))
                .inProgressComplaints(
                        complaintRepository.countByStatus(ComplaintStatus.IN_PROGRESS))
                .resolvedComplaints(
                        complaintRepository.countByStatus(ComplaintStatus.RESOLVED))
                .closedComplaints(
                        complaintRepository.countByStatus(ComplaintStatus.CLOSED))

                .slaBreachedComplaints(
                        complaintRepository.countBySlaBreachedTrue())

                .engineersCount(
                        userRepository.countByRole(Role.ENGINEER))
                .leadsCount(
                        userRepository.countByRole(Role.LEAD))
                .managersCount(
                        userRepository.countByRole(Role.MANAGER))

                .build();
    }
    
    
    //REASSIGN LOGIC ONLY FOR LEAD	
    
    @Override
    public ComplaintResponseDto reassignComplaint(
    		Long complaintId,
    		ReassignComplaintRequestDto dto) {
    	
    	
    	Complaint complaint=complaintRepository.findById(complaintId)
    			.orElseThrow(()->new ResourceNotFoundException("Complaint not found"));
    	
    	if(complaint.getStatus()==ComplaintStatus.RESOLVED||
    			complaint.getStatus()==ComplaintStatus.CLOSED) {
    		throw new InvalidOperationException("Resolved or closed complaints cannot be reassigned");
    	}
    	
    	User newEngineer=userRepository.findById(dto.getEngineerId())
    			.orElseThrow(()-> new ResourceNotFoundException("Engineer not Found"));
    	
    	
    	if(newEngineer.getRole()!=Role.ENGINEER) {
    	
    		throw new InvalidOperationException("User must have ENGINEER role");
    	}
    	
    	complaint.setAssignedTo(newEngineer);
    	
    	Complaint saved = complaintRepository.save(complaint);
    	
    	  saveHistory(complaint, complaint.getStatus(), complaint.getStatus(), getCurrentUser());

    	return ComplaintResponseDto.builder()
    			.id(saved.getId())
    			.title(saved.getTitle())
    			.status(saved.getStatus())
    			.build();
    	
    		
    }
    
    
    //	ESCALATING COMPLAINT BY LEAD	
    @Override
    public ComplaintResponseDto escalateComplaint(Long complaintId) {
    	
    	
    	Complaint complaint=complaintRepository.findById(complaintId)
    			.orElseThrow(()-> new ResourceNotFoundException("complaint not found"));
    	
    	if(complaint.getStatus()==ComplaintStatus.RESOLVED||
    			complaint.getStatus()==ComplaintStatus.CLOSED) {
    		throw new InvalidOperationException("cannot escalate resolved/closed complaint");
    	}
    	
    	Priority current=complaint.getPriority();
    	Priority escalated=current.escalate();
    	
    	if(current==escalated) {
    		throw new InvalidOperationException("Complaint already at highest Priority");
    	}
    	
    	complaint.setPriority(escalated);
    	
    	LocalDateTime  now=LocalDateTime.now();
    	
    	complaint.setSlaDeadline(calculateSlaDeadline(now,escalated));
    	
    	Complaint saved = complaintRepository.save(complaint);
    	
        saveHistory(complaint, complaint.getStatus(), complaint.getStatus(), getCurrentUser());
    	return ComplaintResponseDto.builder()
    			.id(saved.getId())
    			.title(saved.getTitle())
    			.description(saved.getDescription())
    			.status(saved.getStatus())
    			.build();
    }
    
    
    //ACCESING SLA BREACHED COMPLAINT BY MANAGER
    @Override
    public List<ComplaintResponseDto> getSlaBreachedComplaints() {

        return complaintRepository.findBySlaBreachedTrue()
                .stream()
                .map(c -> ComplaintResponseDto.builder()
                        .id(c.getId())
                        .title(c.getTitle())
                        .status(c.getStatus())
                        .build())
                .toList();
    }
    
    
    //MANAGER CLOSING THE RESOLVED COMPLAINTS TO CLOSED
    	
    	public ComplaintResponseDto closeComplaints(Long ComplaintID) {
    		
    		Complaint complaint=complaintRepository.findById(ComplaintID)
    				.orElseThrow(()->new ResourceNotFoundException("Complaint not found"));
    		
    		if(complaint.getStatus()!=ComplaintStatus.RESOLVED) {
    			throw new InvalidOperationException("Only RESOLVED complaints can be solved");
    		}
    		 ComplaintStatus oldStatus = complaint.getStatus();
    		complaint.setStatus(ComplaintStatus.CLOSED);
    		Complaint saved = complaintRepository.save(complaint);
    		 saveHistory(complaint, oldStatus, ComplaintStatus.CLOSED, getCurrentUser());
    		return ComplaintResponseDto.builder()
    				.id(saved.getId())
    				.title(saved.getTitle())
    				.description(saved.getDescription())
    				.status(saved.getStatus())
    				.build();
    	}
    	
    	//RETRVING THE COMPLAINTLIST BY CUSTOMER
    	
    	@Override
    	public Page<MyComplaintListDto> getMyComplaints(Pageable pageable) {

    	    User currentUser = getCurrentUser();

    	    return complaintRepository
    	            .findByCreatedByOrderByCreatedAtDesc(currentUser,pageable)
    	            		.map(c -> MyComplaintListDto.builder()
    	                    .id(c.getId())
    	                    .title(c.getTitle())
    	                    .createdAt(c.getCreatedAt())
    	                    .build());
    	            
    	}
    	
    	//DETAILD COMPLAINT VIEW FOR CUSTOMER 
    	
    	@Override
    	public CustomerComplaintResponseDto getMyComplaintById(Long complaintId) {
    		User currentUser=getCurrentUser();
    		
    		Complaint complaint=complaintRepository.findById(complaintId)
    				.orElseThrow(()->new ResourceNotFoundException("Complaint not found"));
    		
    		if(!complaint.getCreatedBy().getId().equals(currentUser.getId())) {
    			throw new InvalidOperationException("Access denied");
    		}
    		return CustomerComplaintResponseDto.builder()
    				.id(complaint.getId())
    				.title(complaint.getTitle())
    				.description(complaint.getDescription())
    				.category(complaint.getCategory())
    				.priority(complaint.getPriority())
    				.status(complaint.getStatus())
    				.createdAt(complaint.getCreatedAt())
    				.build();
    	}

    	@Override
    	public List<ComplaintTimelineDto> getComplaintTimeline(Long complaintId){
    		
    		Complaint complaint=complaintRepository.findById(complaintId)
    				.orElseThrow(()->new ResourceNotFoundException("Complaints not found"));
    		
    		return complaintHistoryRepository
    				.findByComplaintOrderByChangedAtDesc(complaint)
    				.stream()
    				.map(h->ComplaintTimelineDto.builder()
    						.oldStatus(h.getOldStatus())
    						.newStatus(h.getNewStatus())
    						.changedAt(h.getChangedAt())
    						.changedByName(h.getChangedBy().getName())
    						.changedByRole(h.getChangedBy().getRole())
    						.build())
    				.toList();
 	
    	}
    	
    	@Override
    	public Page<AdminComplaintListDto> getAllComplaints(Pageable pageable) {

    	    return complaintRepository.findAll(pageable)
    	        .map(c -> AdminComplaintListDto.builder()
    	            .id(c.getId())
    	            .title(c.getTitle())
    	            .status(c.getStatus())
    	            .priority(c.getPriority())
    	            .category(c.getCategory())
    	            .createdAt(c.getCreatedAt())
    	            .build());
    	}

    	@Override
    	public List<ComplaintResponseDto> getAssignedComplaints() {

    	    User engineer = getCurrentUser();

    	    return complaintRepository
    	            .findByAssignedTo(engineer)
    	            .stream()
    	            .map(c -> ComplaintResponseDto.builder()
    	                    .id(c.getId())
    	                    .title(c.getTitle())
    	                    .description(c.getDescription())
    	                    .status(c.getStatus())
    	                    .priority(c.getPriority())
    	                    .category(c.getCategory())
    	                    .build())
    	            .toList();
    	}


    	

    // SLA CALCULATION (CORE)
  
    private LocalDateTime calculateSlaDeadline(
            LocalDateTime createdAt,
            Priority priority) {

        return switch (priority) {
            case HIGH -> createdAt.plusHours(24);
            case MEDIUM -> createdAt.plusHours(48);
            case LOW -> createdAt.plusHours(72);
        };
    }


    // CURRENT USER HELPER
    
    private User getCurrentUser() {

        CustomUserDetails userDetails =
                (CustomUserDetails) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Logged-in user not found"));
    }
    
    
    //HELPER FUNCTION OF SAVEHISTORY
    
    
    private void saveHistory(
            Complaint complaint,
            ComplaintStatus oldStatus,
            ComplaintStatus newStatus,
            User actor) {

        ComplaintHistory history = ComplaintHistory.builder()
                .complaint(complaint)
                .oldStatus(oldStatus)
                .newStatus(newStatus)
                .changedBy(actor)
                .changedAt(LocalDateTime.now())
                .build();

        complaintHistoryRepository.save(history);
    }

}
