package com.company.complaintsystem.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.company.complaintsystem.dto.CustomerRegisterRequestDto;
import com.company.complaintsystem.dto.UserResponseDto;
import com.company.complaintsystem.entity.Role;
import com.company.complaintsystem.entity.User;
import com.company.complaintsystem.exception.ResourceAlreadyExistsException;
import com.company.complaintsystem.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto registerCustomer(CustomerRegisterRequestDto dto) {

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new ResourceAlreadyExistsException(
                    "User already exists with email: " + dto.getEmail());
        }

        User user = User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(Role.CUSTOMER)   // 🔒 FORCED
                .active(true)
                .build();

        User saved = userRepository.save(user);

        return UserResponseDto.builder()
                .id(saved.getId())
                .name(saved.getName())
                .email(saved.getEmail())
                .role(saved.getRole())
                .build();
    }
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));
    }

}
