package com.nhat.ecommerce_backend.dto.product;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class FilterRequest {

    private String name;
    private Long categoryId;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Integer page;
    private Integer size;
}
