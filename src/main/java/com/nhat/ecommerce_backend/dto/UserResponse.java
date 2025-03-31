package com.nhat.ecommerce_backend.dto;

import com.nhat.ecommerce_backend.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponse {
    private String email;
    private String name;
    private String phone;
    private String address;
    private String role;

    public UserResponse(User user) {
        this.email = user.getEmail();
        this.name = user.getName();
        this.phone = user.getPhone();
        this.address = user.getAddress();
        this.role = user.getRole().name();
    }
}

