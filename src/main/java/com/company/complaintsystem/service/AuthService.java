package com.company.complaintsystem.service;

import com.company.complaintsystem.dto.CustomerRegisterRequestDto;
import com.company.complaintsystem.dto.UserResponseDto;
import com.company.complaintsystem.entity.User;

public interface AuthService {

	

	    User findByEmail(String email);



    UserResponseDto registerCustomer(CustomerRegisterRequestDto dto);
}
