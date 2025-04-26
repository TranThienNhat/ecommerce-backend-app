package com.nhat.ecommerce_backend.dto.order;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class OrdersRequest {

    @NotEmpty
    private List<UUID> cartItemList;
}
