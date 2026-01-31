package com.company.complaintsystem.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.company.complaintsystem.dto.CustomerRegisterRequestDto;
import com.company.complaintsystem.dto.LoginRequest;
import com.company.complaintsystem.dto.LoginResponse;
import com.company.complaintsystem.dto.UserResponseDto;
import com.company.complaintsystem.security.JwtUtil;
import com.company.complaintsystem.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final AuthService authService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {

        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
            )
        );

        var user = authService.findByEmail(request.getEmail());

        String token = jwtUtil.generateToken(
            user.getEmail(),
            user.getRole().name()
        );

        return LoginResponse.builder()
                .token(token)
                .build();
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto registerCustomer(
            @Valid @RequestBody CustomerRegisterRequestDto dto) {

        return authService.registerCustomer(dto);
    }
}
