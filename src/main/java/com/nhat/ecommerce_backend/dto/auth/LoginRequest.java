package com.nhat.ecommerce_backend.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @Email(message = "Email không đúng định dạng")
    @Email
    private String email;

    @NotBlank(message = "Mật khẩu không được để trống")
    private String password;
}
