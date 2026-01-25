package com.company.complaintsystem.dto;

import java.time.LocalDateTime;

import com.company.complaintsystem.entity.ComplaintStatus;
import com.company.complaintsystem.entity.Role;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ComplaintTimelineDto {

	
	private ComplaintStatus oldStatus;
	
	private ComplaintStatus newStatus;
	
	private LocalDateTime changedAt;
	
	private String changedByName;
	
	private Role changedByRole;
	
}
