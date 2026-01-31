package com.company.complaintsystem.dto;

import java.time.LocalDateTime;

import com.company.complaintsystem.entity.ComplaintStatus;
import com.company.complaintsystem.entity.Priority;
import com.company.complaintsystem.entity.ComplaintCategory;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminComplaintListDto {

    private Long id;
    private String title;
    private ComplaintStatus status;
    private Priority priority;
    private ComplaintCategory category;
    private LocalDateTime createdAt;
}
