package com.nhat.ecommerce_backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LogingRequest {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;
}
