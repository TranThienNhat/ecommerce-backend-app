package com.nhat.ecommerce_backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CartItemRequest {

    @NotBlank
    private String cartId;

    @Min(1)
    private Long productId;

    @Min(1)
    private Integer quantity;
}
