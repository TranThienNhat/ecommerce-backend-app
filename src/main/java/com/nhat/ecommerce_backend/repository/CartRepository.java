package com.nhat.ecommerce_backend.repository;

import com.nhat.ecommerce_backend.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CartRepository extends JpaRepository<Cart, UUID> {
    Cart findByUserId(Long userId);
}

