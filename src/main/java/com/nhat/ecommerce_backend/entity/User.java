package com.nhat.ecommerce_backend.entity;

import com.nhat.ecommerce_backend.model.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String phone;

    private String address;

    private LocalDateTime time;

    @Column(unique = true, nullable = false)
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;
}
