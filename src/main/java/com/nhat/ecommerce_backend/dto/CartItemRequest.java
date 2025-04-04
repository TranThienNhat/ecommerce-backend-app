package com.nhat.ecommerce_backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
public class CartItemRequest {

    @NotBlank
    private String cartId;

    @NotBlank
    private Long productId;

    @NotBlank
    private int quantity;
}
