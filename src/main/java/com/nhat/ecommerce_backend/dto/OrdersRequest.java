package com.nhat.ecommerce_backend.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class OrdersRequest {

    @NotNull
    private Long userId;

    @NotEmpty
    private List<UUID> cartItemList;
}
