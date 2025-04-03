package com.nhat.ecommerce_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    @Id
    @UuidGenerator
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    private int quantity;
    private BigDecimal price;
}
