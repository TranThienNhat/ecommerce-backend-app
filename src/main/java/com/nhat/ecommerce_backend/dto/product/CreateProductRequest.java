package com.nhat.ecommerce_backend.dto.product;

import com.nhat.ecommerce_backend.model.enums.ProductStatus;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class CreateProductRequest {

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

    @NotNull(message = "Số lượng không được để trống!")
    @Min(value = 0, message = "Số lượng không được âm!")
    private Integer quantity;

    private Boolean isFeatured = false;

    @DecimalMin(value = "0.0", message = "Giảm giá phải lớn hơn hoặc bằng 0")
    @DecimalMax(value = "100.0", message = "Giảm giá không được vượt quá 100")
    private BigDecimal discountPercent;

    private String brand;

    private ProductStatus status = ProductStatus.ACTIVE;
}

