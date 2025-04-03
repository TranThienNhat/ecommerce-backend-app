package com.nhat.ecommerce_backend.controller;

import com.nhat.ecommerce_backend.config.JwtUtil;
import com.nhat.ecommerce_backend.dto.AuthResponse;
import com.nhat.ecommerce_backend.dto.LoginRequest;
import com.nhat.ecommerce_backend.dto.RegisterRequest;
import com.nhat.ecommerce_backend.entity.User;
import com.nhat.ecommerce_backend.service.CartService;
import com.nhat.ecommerce_backend.service.CustomDetailService;
import com.nhat.ecommerce_backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final CustomDetailService customDetailService;
    private final CartService cartService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest request) {
        try {
            var user = userService.registerUser(request);
            cartService.createCart(user.getId());
            return ResponseEntity.ok("Register thành công !");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            // Lấy UserDetails từ CustomDetailService
            UserDetails userDetails = customDetailService.loadUserByUsername(request.getEmail());
            var token = jwtUtil.generateToken(userDetails);

            return ResponseEntity.ok(new AuthResponse(token));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body("Sai email hoặc mật khẩu !");
        }
    }

}

