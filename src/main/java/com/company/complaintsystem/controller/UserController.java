package com.company.complaintsystem.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.company.complaintsystem.dto.UserRequestDto;
import com.company.complaintsystem.dto.UserResponseDto;
import com.company.complaintsystem.service.UserServiceImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
	
	private final UserServiceImpl userServiceImpl;
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public UserResponseDto createUser(@Valid @RequestBody UserRequestDto dto){
		return userServiceImpl.createUser(dto);
	}

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','LEAD')")
    public List<UserResponseDto> getAllUsers() {
        return userServiceImpl.getAllUsers();
    }
    
    @GetMapping("/engineers")
    @PreAuthorize("hasRole('LEAD')")
    public List<UserResponseDto> getEngineers() {
        return userServiceImpl.getEngineers();
    }

}
