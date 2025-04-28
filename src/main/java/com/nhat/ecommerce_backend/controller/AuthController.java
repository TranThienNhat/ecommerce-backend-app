package com.nhat.ecommerce_backend.controller;

import com.nhat.ecommerce_backend.dto.auth.LoginResponse;
import com.nhat.ecommerce_backend.dto.auth.LoginRequest;
import com.nhat.ecommerce_backend.dto.refreshtoken.RefreshTokenRequest;
import com.nhat.ecommerce_backend.dto.refreshtoken.RefreshTokenResponse;
import com.nhat.ecommerce_backend.dto.user.RegisterRequest;
import com.nhat.ecommerce_backend.service.auth.AuthenticationService;
import com.nhat.ecommerce_backend.service.refreshtoken.RefreshTokenService;
import com.nhat.ecommerce_backend.service.user.UserService;
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
    private final AuthenticationService authenticationservice;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid RegisterRequest request) {
        userService.registerUser(request);
        return ResponseEntity.ok("Registration successful!");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        var token = authenticationservice.login(request);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@RequestBody @Valid RefreshTokenRequest request) {
        RefreshTokenResponse response = refreshTokenService.createNewAccessToken(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        refreshTokenService.logout();
        return ResponseEntity.ok("Logout successful");
    }
}

