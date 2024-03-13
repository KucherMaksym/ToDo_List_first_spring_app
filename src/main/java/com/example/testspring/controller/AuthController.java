package com.example.testspring.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    @PostMapping("/login")
    public ResponseEntity<String> authenticate(Authentication authentication) {
        // Обработка успешной аутентификации
        return ResponseEntity.ok("Authentication successful");
    }
}