package com.nhat.ecommerce_backend.service.product;

import com.nhat.ecommerce_backend.dto.product.CreateProductRequest;
import com.nhat.ecommerce_backend.dto.product.FilterRequest;
import com.nhat.ecommerce_backend.dto.product.SearchRequest;
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


    public void updateProduct(UpdateProductRequest request) {
        Product product = productRepository.findById(request.getId())
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

    public List<Product> searchProducts(SearchRequest request) {
        return productRepository.findByNameContainingIgnoreCase(request.getKeywork());
    }

    public Page<Product> filterByConditions(FilterRequest request) {
        Pageable pageable = PageRequest.of(
                request.getPage() != null ? request.getPage() : 0,
                request.getSize() != null ? request.getSize() : 20
        );

        return productRepository.filterByConditions(
                request.getName(),
                request.getCategoryId(),
                request.getMinPrice(),
                request.getMaxPrice(),
                pageable
        );
    }

}
