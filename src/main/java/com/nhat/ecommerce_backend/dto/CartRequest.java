package com.nhat.ecommerce_backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CartRequest {

    @NotBlank
    private Long productId;

    @NotBlank
    private int quantity;
}
