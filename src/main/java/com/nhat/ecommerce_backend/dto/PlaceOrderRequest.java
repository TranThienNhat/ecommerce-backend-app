package com.nhat.ecommerce_backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PlaceOrderRequest {

    @NotNull
    private Long userId;  // ID của người dùng

    @NotNull
    private Long productId;  // ID của sản phẩm

    @Min(1)
    private Integer quantity;  // Số lượng sản phẩm
}

