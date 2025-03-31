package com.nhat.ecommerce_backend.service;

import com.nhat.ecommerce_backend.model.enums.Role;
import com.nhat.ecommerce_backend.dto.RegisterRequest;
import com.nhat.ecommerce_backend.entity.User;
import com.nhat.ecommerce_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerUser(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Tên email đã tồn tại !");
        }

        User user = new User();
        user.setName(request.getName());
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());
        user.setTime(LocalDateTime.now());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);

        userRepository.save(user);
    }

    public User getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationCredentialsNotFoundException("Người dùng chưa xác thực");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails userDetails) {
            String email = userDetails.getUsername();
            return userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
        }

        throw new RuntimeException("Không thể xác định người dùng");
    }
}
