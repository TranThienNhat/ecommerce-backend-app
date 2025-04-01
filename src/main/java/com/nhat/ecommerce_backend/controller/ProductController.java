package com.nhat.ecommerce_backend.controller;

import com.nhat.ecommerce_backend.dto.ProductRequest;
import com.nhat.ecommerce_backend.entity.Product;
import com.nhat.ecommerce_backend.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody @Valid ProductRequest request) {
        try {
            Product newproduct = productService.createProduct(request);
            return ResponseEntity.ok(newproduct);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody @Valid ProductRequest request) {
        try {
            Product updatedProduct = productService.updateProduct(id, request);
            return ResponseEntity.ok(updatedProduct);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok("Xóa product thành công !");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
