package com.company.complaintsystem.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/health")
    public String health() {
        return "OK";
    }
        @GetMapping("/")
    public String home() {
        return "Complaint & SLA Management System API is running.";
    }
}
