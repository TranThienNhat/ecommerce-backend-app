package com.nhat.ecommerce_backend.controller;

import com.nhat.ecommerce_backend.dto.UserResponse;
import com.nhat.ecommerce_backend.entity.User;
import com.nhat.ecommerce_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")

public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping("/user/me")
    public ResponseEntity<?> getProfile() {
        try {
            User user = userService.getProfile();
            return ResponseEntity.ok(new UserResponse(user));
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
}
