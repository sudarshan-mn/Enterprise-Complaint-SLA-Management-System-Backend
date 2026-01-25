package com.company.complaintsystem.dto;

import com.company.complaintsystem.entity.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class UserRequestDto {
	
	@NotBlank
	private String name;
	
	@Email
	@NotBlank
	private String email;
	
	
	@NotBlank
	private String password;
	
	private Role role;

		}
	
	
