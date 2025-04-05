package com.nhat.ecommerce_backend.dto;

import com.nhat.ecommerce_backend.model.enums.Status;
import lombok.Data;

@Data
public class UpdateOrderRequest {
    private Status status;
}
