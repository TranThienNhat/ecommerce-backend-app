package com.nhat.ecommerce_backend.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CartItemDeleteRequest {

    @NotEmpty(message = "Danh sách cartItemsId không được rỗng")
    private List<UUID> cartItemsId;
}

