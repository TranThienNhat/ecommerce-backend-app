package com.nhat.ecommerce_backend.service.product;

import com.nhat.ecommerce_backend.dto.product.CreateProductRequest;
import com.nhat.ecommerce_backend.dto.product.UpdateProductRequest;
import com.nhat.ecommerce_backend.entity.Product;
import com.nhat.ecommerce_backend.exception.BusinessException;
import com.nhat.ecommerce_backend.repository.ProductRepository;
import com.nhat.ecommerce_backend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final ProductMapper productMapper;

    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }

    public List<Product> getAllProductByCategory(Long id) {
        log.info("Fetching products by categoryId = {}", id);
        return productRepository.findByCategoryId(id);
    }

    public void createProduct(CreateProductRequest request) {
        log.info("Creating new product with name = {}", request.getName());
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
        log.info("Product after creation: {}", product.getId());
    }


    public void updateProduct(Long id,UpdateProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Product not found"));

        log.info("Updating product with ID = {}, new data: {}", id, request);
        productMapper.updateProductFromDto(request, product);
        productRepository.save(product);
        log.info("Product updated successfully with ID = {}", product.getId());
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                        .orElseThrow(() -> new BusinessException("Product not found"));

        log.info("Deleting product with ID = {}, Name = {}", product.getId(), product.getName());
        productRepository.delete(product);
        log.info("Product with ID = {} has been deleted", product.getId());
    }

    public Product getProductById(Long Id) {
        log.info("Fetching product by ID = {}", Id);
        return productRepository.findById(Id)
                .orElseThrow(() -> new BusinessException("Product not found"));
    }

    public List<Product> searchProducts(String keyword) {
        List<Product> results = productRepository.findByNameContainingIgnoreCase(keyword);
        log.info("Searching for products with keyword: {}, found {} results", keyword, results.size());
        return results;
    }

    @Override
    public Page<Product> filterByConditions(String name, Long categoryId, BigDecimal minPrice, BigDecimal maxPrice, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        log.info("Filtering products with conditions: name = {}, categoryId = {}, minPrice = {}, maxPrice = {}, page = {}, size = {}",
                name, categoryId, minPrice, maxPrice, page, size);
        Page<Product> result = productRepository.filterByConditions(name, categoryId, minPrice, maxPrice, pageable);
        log.info("Found {} products matching the filter", result.getTotalElements());
        return result;
    }
}
