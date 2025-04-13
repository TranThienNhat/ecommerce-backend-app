package com.nhat.ecommerce_backend.controller;

import com.nhat.ecommerce_backend.dto.product.CreateProductRequest;
import com.nhat.ecommerce_backend.dto.product.FilterRequest;
import com.nhat.ecommerce_backend.dto.product.SearchRequest;
import com.nhat.ecommerce_backend.dto.product.UpdateProductRequest;
import com.nhat.ecommerce_backend.entity.Product;
import com.nhat.ecommerce_backend.service.product.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<Product>> getAllProduct() {
        return ResponseEntity.ok(productService.getAllProduct());
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<List<Product>> getAllProductByCategory(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getAllProductByCategory(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<String> createProduct(@RequestBody @Valid CreateProductRequest request) {
        productService.createProduct(request);
        return ResponseEntity.ok("Create successful products !");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    public ResponseEntity<String> updateProduct(@RequestBody @Valid UpdateProductRequest request) {
        productService.updateProduct(request);
        return ResponseEntity.ok("Product update successful");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully");
    }

    @PostMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestBody SearchRequest request) {
        return ResponseEntity.ok(productService.searchProducts(request));
    }

    @PostMapping("/filter")
    public ResponseEntity<Page<Product>> filterByConditions(@RequestBody FilterRequest request) {
        Page<Product> page = productService.filterByConditions(request);
        return ResponseEntity.ok(page);
    }
}