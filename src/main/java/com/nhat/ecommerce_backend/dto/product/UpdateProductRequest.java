package com.nhat.ecommerce_backend.dto.product;

import com.nhat.ecommerce_backend.model.enums.ProductStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateProductRequest {
    private String name;
    private BigDecimal price;
    private Integer quantity;
    private String description;
    private String imageUrl;
    private Boolean isFeatured;
    private BigDecimal discountPercent;
    private ProductStatus status;
    private String brand;
}
