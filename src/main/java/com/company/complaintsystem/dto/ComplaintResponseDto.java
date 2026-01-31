package com.company.complaintsystem.dto;

import com.company.complaintsystem.entity.ComplaintCategory;
import com.company.complaintsystem.entity.ComplaintStatus;
import com.company.complaintsystem.entity.Priority;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ComplaintResponseDto {

    private Long id;
    private String title;
    private String description;
    private ComplaintStatus status;
    
    private Priority priority;
    
    private ComplaintCategory category;
    
}
