package com.company.complaintsystem.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@Setter
public class MyComplaintListDto {

	
	private Long id;
	
	private String title;
	
	private LocalDateTime createdAt;
}
