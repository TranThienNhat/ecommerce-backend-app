package com.nhat.ecommerce_backend.dto.product;

import com.nhat.ecommerce_backend.model.enums.ProductStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class UpdateProductRequest {
    private String name;
    private BigDecimal price;
    private Integer quantity;
    private String description;
    private String imageUrl;
    private Boolean isFeatured;
    private List<Long> categoryIds;
    private BigDecimal discountPercent;
    private ProductStatus status;
    private String brand;
}
