package com.company.complaintsystem.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.PageRequest;
import com.company.complaintsystem.dto.AssignComplaintRequestDto;
import com.company.complaintsystem.dto.ComplaintCreateRequestDto;
import com.company.complaintsystem.dto.ComplaintResponseDto;
import com.company.complaintsystem.dto.ComplaintTimelineDto;
import com.company.complaintsystem.dto.CustomerComplaintResponseDto;
import org.springframework.data.domain.Pageable; 
import com.company.complaintsystem.dto.MyComplaintListDto;
import com.company.complaintsystem.dto.ReassignComplaintRequestDto;
import com.company.complaintsystem.service.ComplaintServiceImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/complaints")
@RequiredArgsConstructor
public class ComplaintController {
	
	private final ComplaintServiceImpl complaintService;
	
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@PreAuthorize("hasRole('CUSTOMER')")
	public ComplaintResponseDto createComplatint(@Valid @RequestBody ComplaintCreateRequestDto dto ) {
		return complaintService.createComplaint(dto);
	}
	
	@PutMapping("/{id}/assign")
	@PreAuthorize("hasRole('ADMIN')")
	public ComplaintResponseDto assaignComplaint(@PathVariable Long id,@Valid @RequestBody AssignComplaintRequestDto dto ) {
		
		return complaintService.assignComplaint(id, dto);
	}
	
	@PutMapping("/{id}/start")
	@PreAuthorize("hasRole('ENGINEER')")
	public void startComplaint(@PathVariable Long id) {
		complaintService.startComplaint(id);
	}
	
	@PutMapping("/{id}/resolve")
	@PreAuthorize("hasRole('ENGINEER')")
	public void resolveComplaint(@PathVariable Long id) {
		complaintService.resolveComplaint(id);
	}
	
	@PutMapping("/{id}/reassign")
	@PreAuthorize("hasRole('LEAD')")
	public ComplaintResponseDto reassignComplaint(@PathVariable Long id,
			@RequestBody ReassignComplaintRequestDto dto) {
		return complaintService.reassignComplaint(id, dto);
	}
	@PutMapping("/{id}/escalate")
	@PreAuthorize("hasRole('LEAD')")
	public ComplaintResponseDto escalateComplaint(@PathVariable Long id) {
		return complaintService.escalateComplaint(id);
	}
	@GetMapping("/my")
	@PreAuthorize("hasRole('CUSTOMER')")
	public Page<MyComplaintListDto> getMyComplaints(
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "5") int size) {

	    Pageable pageable = PageRequest.of(page, size);
	    return complaintService.getMyComplaints(pageable);
	}


	
	@GetMapping("/my/{id}")
	@PreAuthorize("hasRole('CUSTOMER')")
	public CustomerComplaintResponseDto getMyComplaintById(@PathVariable Long id) {
		return complaintService.getMyComplaintById(id);
	}
	
	@GetMapping("/{id}/timeline")
	@PreAuthorize("hasAnyRole('ADMIN','LEAD','MANAGER')")
	public List<ComplaintTimelineDto> getComplaintTimeline(@PathVariable Long id){
		return complaintService.getComplaintTimeline(id);
	}
}
