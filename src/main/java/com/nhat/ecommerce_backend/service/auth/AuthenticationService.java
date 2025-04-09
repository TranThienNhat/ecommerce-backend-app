package com.nhat.ecommerce_backend.service.auth;

import com.nhat.ecommerce_backend.dto.auth.LoginRequest;
import com.nhat.ecommerce_backend.dto.auth.LoginResponse;

public interface AuthenticationService {
    LoginResponse login(LoginRequest request);
}
