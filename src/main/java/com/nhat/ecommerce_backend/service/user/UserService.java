package com.nhat.ecommerce_backend.service.user;

import com.nhat.ecommerce_backend.dto.user.RegisterRequest;
import com.nhat.ecommerce_backend.entity.User;

public interface UserService {
    void registerUser(RegisterRequest request);
    User getProfile();
    User findById(Long userId);
    User getByEmail(String username);
}
