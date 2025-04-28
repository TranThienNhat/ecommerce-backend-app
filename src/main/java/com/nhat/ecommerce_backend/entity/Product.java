package com.nhat.ecommerce_backend.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.nhat.ecommerce_backend.model.enums.ProductStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private BigDecimal price;
    private String description;
    private String imageUrl;

    private Integer quantity;
    private String sku;
    private Boolean isFeatured;
    private BigDecimal discountPercent;
    private String brand;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToMany
    @JoinTable(
            name = "product_category",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    @JsonManagedReference
    private List<Category> categories = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.sku = "PRD-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}