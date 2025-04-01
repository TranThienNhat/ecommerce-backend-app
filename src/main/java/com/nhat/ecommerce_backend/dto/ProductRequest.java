package com.nhat.ecommerce_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductRequest {

    @NotBlank(message = "Tên sản phẩm không được để trống!")
    @Size(min = 3, max = 100, message = "Tên sản phẩm phải có độ dài từ 3 đến 100 ký tự!")
    private String name;

    @NotBlank(message = "Mô tả sản phẩm không được để trống!")
    private String description;

    @NotBlank(message = "Ảnh sản phẩm không được để trống!")
    private String imageUrl;

    @NotNull(message = "Giá sản phẩm không được để trống!")
    @Positive(message = "Giá sản phẩm phải lớn hơn 0!")
    private BigDecimal price;

    @NotNull(message = "ID danh mục không được để trống!")
    private Long categoryId;
}
