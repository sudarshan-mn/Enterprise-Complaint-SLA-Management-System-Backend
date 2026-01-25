package com.company.complaintsystem.dto;

import com.company.complaintsystem.entity.ComplaintStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ComplaintResponseDto {

    private Long id;
    private String title;
    private String description;
    private ComplaintStatus status;
}
