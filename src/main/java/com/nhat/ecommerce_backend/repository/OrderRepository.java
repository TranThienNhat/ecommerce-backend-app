package com.nhat.ecommerce_backend.repository;

import com.nhat.ecommerce_backend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
}
