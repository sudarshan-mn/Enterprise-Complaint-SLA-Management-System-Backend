package com.company.complaintsystem.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignComplaintRequestDto {

	@NotNull
	private Long engineerId;
}
