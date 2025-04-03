package com.nhat.ecommerce_backend.repository;

import com.nhat.ecommerce_backend.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CartItemRepository extends JpaRepository<CartItem, UUID> {
}
