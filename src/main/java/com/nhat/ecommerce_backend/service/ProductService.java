package com.nhat.ecommerce_backend.service;

import com.nhat.ecommerce_backend.dto.ProductRequest;
import com.nhat.ecommerce_backend.entity.Category;
import com.nhat.ecommerce_backend.entity.Product;
import com.nhat.ecommerce_backend.repository.CategoryRepository;
import com.nhat.ecommerce_backend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }

    public List<Product> getAllProductByCategory(Long id) {
        return productRepository.findByCategoryId(id);
    }

    public Product productFromRequest(Product product, ProductRequest request, boolean isNewProduct) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(()-> new RuntimeException("Category không tồn tại !"));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        if (isNewProduct) {
            product.setCreateAt(LocalDateTime.now());
        }
        product.setImageUrl(request.getImageUrl());
        product.setCategory(category);

        return product;
    }

    public Product createProduct(ProductRequest request) {
        Product product = productFromRequest(new Product(), request, true);
        return productRepository.save(product);
    }


    public Product updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm !"));

        productFromRequest(product, request, false);
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy product để xóa !");
        }
        productRepository.deleteById(id);
    }
}
