package com.company.complaintsystem.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.company.complaintsystem.dto.AdminComplaintListDto;
import com.company.complaintsystem.dto.AdminDashboardResponseDto;
import com.company.complaintsystem.dto.AssignComplaintRequestDto;
import com.company.complaintsystem.dto.ComplaintCreateRequestDto;
import com.company.complaintsystem.dto.ComplaintResponseDto;
import com.company.complaintsystem.dto.ComplaintTimelineDto;
import com.company.complaintsystem.dto.CustomerComplaintResponseDto;

import com.company.complaintsystem.dto.MyComplaintListDto;
import com.company.complaintsystem.dto.ReassignComplaintRequestDto;
import com.company.complaintsystem.entity.Complaint;
import com.company.complaintsystem.entity.ComplaintStatus;

public interface ComplaintService {

	
	ComplaintResponseDto createComplaint (ComplaintCreateRequestDto dto);
	
	ComplaintResponseDto assignComplaint(Long complaintId,AssignComplaintRequestDto dto);
	
	void startComplaint(Long complaintId);
	void resolveComplaint(Long complaintId);
	
	AdminDashboardResponseDto getAdminDashboard();
	
	ComplaintResponseDto reassignComplaint(Long complaintId,ReassignComplaintRequestDto dto);
	
	ComplaintResponseDto escalateComplaint(Long complaintId);
	
	List<ComplaintResponseDto> getSlaBreachedComplaints();
	
	ComplaintResponseDto closeComplaints(Long complaintId);
	
	Page<MyComplaintListDto> getMyComplaints(Pageable pageable);
	
	
	CustomerComplaintResponseDto getMyComplaintById(Long complaintId);
	
	List<ComplaintTimelineDto> getComplaintTimeline(Long complaintId);
	
	 Page<AdminComplaintListDto> getAllComplaints(Pageable pageable);
	
	 List<ComplaintResponseDto> getAssignedComplaints();
	 List<ComplaintResponseDto> getResolvedComplaints();

}
