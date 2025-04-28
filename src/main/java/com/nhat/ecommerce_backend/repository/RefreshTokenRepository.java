package com.nhat.ecommerce_backend.repository;

import com.nhat.ecommerce_backend.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    RefreshToken findByRefreshToken(String refreshToken);
    RefreshToken findByUserId(Long id);
}
