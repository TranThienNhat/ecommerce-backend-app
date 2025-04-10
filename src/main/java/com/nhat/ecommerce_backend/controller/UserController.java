package com.nhat.ecommerce_backend.controller;

import com.nhat.ecommerce_backend.dto.user.response.UserResponse;
import com.nhat.ecommerce_backend.entity.User;
import com.nhat.ecommerce_backend.service.user.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userServiceImpl;

    @GetMapping("/user/me")
    public ResponseEntity<UserResponse> getProfile() {
        User user = userServiceImpl.getProfile();
        return ResponseEntity.ok(new UserResponse(user));
    }
}
