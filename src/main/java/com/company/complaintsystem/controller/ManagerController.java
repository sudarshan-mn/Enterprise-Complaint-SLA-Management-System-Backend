package com.company.complaintsystem.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.complaintsystem.dto.ComplaintResponseDto;
import com.company.complaintsystem.service.ComplaintService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/manager")
@RequiredArgsConstructor
public class ManagerController {

	
	private final ComplaintService complaintService;
	
	@GetMapping("/sla-breached")
	@PreAuthorize("hasRole('MANAGER')")
	public List<ComplaintResponseDto> slaBreachedComplaints(){
		return complaintService.getSlaBreachedComplaints();
	}
	
	@PutMapping("/complaints/{id}/close")
	@PreAuthorize("hasRole('MANAGER')")
	public ComplaintResponseDto closeComplaints(@PathVariable Long id) {
		return complaintService.closeComplaints(id);
	}
	@GetMapping("/resolved")
	@PreAuthorize("hasRole('MANAGER')")
	public List<ComplaintResponseDto> getResolvedComplaints() {
	    return complaintService.getResolvedComplaints();
	}

	
}
