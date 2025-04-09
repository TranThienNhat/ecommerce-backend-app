package com.nhat.ecommerce_backend.controller;

import com.nhat.ecommerce_backend.dto.auth.LoginResponse;
import com.nhat.ecommerce_backend.dto.auth.LoginRequest;
import com.nhat.ecommerce_backend.dto.user.request.RegisterRequest;
import com.nhat.ecommerce_backend.service.auth.AuthenticationServiceImpl;
import com.nhat.ecommerce_backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthenticationServiceImpl authenticationserviceimpl;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest request) {
        userService.registerUser(request);
        return ResponseEntity.ok("Registration successful!");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        var token = authenticationserviceimpl.login(request);
        return ResponseEntity.ok(token);
    }
}

