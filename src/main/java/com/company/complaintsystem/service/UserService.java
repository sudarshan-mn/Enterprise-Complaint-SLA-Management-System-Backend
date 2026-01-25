package com.company.complaintsystem.service;

import com.company.complaintsystem.dto.UserRequestDto;
import com.company.complaintsystem.dto.UserResponseDto;

public interface UserService {
  UserResponseDto createUser(UserRequestDto dto);
}
