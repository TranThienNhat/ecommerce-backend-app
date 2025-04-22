package com.nhat.ecommerce_backend.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponse {
    private UUID id;
    private Long productId;
    private String productName;
    private String productImage;
    private int quantity;
    private BigDecimal price;
    private BigDecimal total;
}

