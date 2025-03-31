package com.nhat.ecommerce_backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String phone;

    @NotBlank
    private String address;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;
}
