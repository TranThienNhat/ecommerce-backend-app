package com.nhat.ecommerce_backend.service.product;

import com.nhat.ecommerce_backend.dto.product.CreateProductRequest;
import com.nhat.ecommerce_backend.dto.product.UpdateProductRequest;
import com.nhat.ecommerce_backend.entity.Product;
import com.nhat.ecommerce_backend.exception.BusinessException;
import com.nhat.ecommerce_backend.repository.ProductRepository;
import com.nhat.ecommerce_backend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final ProductMapper productMapper;

    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }

    public List<Product> getAllProductByCategory(Long id) {
        return productRepository.findByCategoryId(id);
    }

    public void createProduct(CreateProductRequest request) {
        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .imageUrl(request.getImageUrl())
                .price(request.getPrice())
                .quantity(request.getQuantity())
                .discountPercent(request.getDiscountPercent())
                .isFeatured(request.getIsFeatured())
                .brand(request.getBrand())
                .category(categoryService.getById(request.getCategoryId()))
                .build();

        productRepository.save(product);
    }


    public void updateProduct(Long id,UpdateProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Product not found"));

        productMapper.updateProductFromDto(request, product);
        productRepository.save(product);

    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                        .orElseThrow(() -> new BusinessException("Product not found"));

        productRepository.delete(product);
    }

    public Product getProductById(Long Id) {
        return productRepository.findById(Id)
                .orElseThrow(() -> new BusinessException("Product not found"));
    }

    public List<Product> searchProducts(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword);
    }

    @Override
    public Page<Product> filterByConditions(String name, Long categoryId, BigDecimal minPrice, BigDecimal maxPrice, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.filterByConditions(name, categoryId, minPrice, maxPrice, pageable);
    }

}
