package com.company.complaintsystem.dto;

import java.time.LocalDateTime;

import com.company.complaintsystem.entity.ComplaintCategory;
import com.company.complaintsystem.entity.ComplaintStatus;
import com.company.complaintsystem.entity.Priority;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CustomerComplaintResponseDto {

	
	private Long id;
	
	private String title;
	
	private String description;
	
	private ComplaintCategory category;
	
    private Priority priority;
    
    
    private ComplaintStatus status;
    
    private LocalDateTime createdAt;
}
