package com.nhat.ecommerce_backend.service.product;

import com.nhat.ecommerce_backend.dto.product.CreateProductRequest;
import com.nhat.ecommerce_backend.dto.product.UpdateProductRequest;
import com.nhat.ecommerce_backend.entity.Product;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    List<Product> getAllProduct();
    List<Product> getAllProductByCategory(Long id);
    void createProduct(CreateProductRequest request);
    void updateProduct(Long id,UpdateProductRequest request);
    void deleteProduct(Long id);
    Product getProductById(Long id);
    List<Product> searchProducts(String request);
    Page<Product> filterByConditions(String name, Long categoryId, BigDecimal minPrice,BigDecimal maxPrice, int page, int size);
}
