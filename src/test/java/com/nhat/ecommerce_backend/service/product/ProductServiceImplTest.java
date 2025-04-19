package com.nhat.ecommerce_backend.service.product;

import com.nhat.ecommerce_backend.dto.product.CreateProductRequest;
import com.nhat.ecommerce_backend.dto.product.UpdateProductRequest;
import com.nhat.ecommerce_backend.entity.Category;
import com.nhat.ecommerce_backend.entity.Product;
import com.nhat.ecommerce_backend.exception.BusinessException;
import com.nhat.ecommerce_backend.model.enums.ProductStatus;
import com.nhat.ecommerce_backend.repository.ProductRepository;
import com.nhat.ecommerce_backend.service.category.CategoryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryService categoryService;

    @Spy
    private ProductMapper productMapper = Mappers.getMapper(ProductMapper.class);


    @InjectMocks
    private ProductServiceImpl productServiceImpl;

    @Test
    void getAllProduct_ShouldReturnList() {

        List<Product> mockProducts = List.of(new Product(), new Product());
        Mockito.when(productRepository.findAll()).thenReturn(mockProducts);

        List<Product> results = productServiceImpl.getAllProduct();

        Assertions.assertEquals(2, results.size());
        Mockito.verify(productRepository).findAll();
    }

    @Test
    void getAllProductByCategory_ShouldReturnList() {
        Long categoryId = 1L;
        List<Product> mockProducts = List.of(new Product(), new Product());

        Mockito.when(productRepository.findByCategoryId(categoryId)).thenReturn(mockProducts);

        List<Product> results = productServiceImpl.getAllProductByCategory(categoryId);

        Assertions.assertEquals(2, results.size());
        Mockito.verify(productRepository).findByCategoryId(categoryId);
    }

    @Test
    void getProductById_ShouldThrow_WhenNotFound() {
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.empty());

        BusinessException exception = Assertions.assertThrows(BusinessException.class, () -> {
            productServiceImpl.getProductById(1L);
        });
        Assertions.assertEquals("Product not found", exception.getMessage());

        Mockito.verify(productRepository).findById(1L);
    }

    @Test
    void createProduct_ShouldCreateProduct() {

        CreateProductRequest request = new CreateProductRequest();
        request.setName("test");
        request.setDescription("product test");
        request.setImageUrl("url test");
        request.setPrice(BigDecimal.valueOf(500));
        request.setCategoryIds(List.of(1L, 2L));
        request.setQuantity(10);
        request.setIsFeatured(false);
        request.setDiscountPercent(BigDecimal.valueOf(0));
        request.setBrand("");
        request.setStatus(ProductStatus.ACTIVE);

        List<Long> mockCategoryId = List.of(1L, 2L);

        List<Category> mockCategory = List.of(new Category(), new Category());

        Mockito.when(categoryService.getAllCategoryById(mockCategoryId)).thenReturn(mockCategory);

        productServiceImpl.createProduct(request);

        ArgumentCaptor<Product> productArgumentCaptor = ArgumentCaptor.forClass(Product.class);
        Mockito.verify(productRepository).save(productArgumentCaptor.capture());

        Product saveProduct = productArgumentCaptor.getValue();
        Assertions.assertEquals("test", saveProduct.getName());
        Assertions.assertEquals("product test", saveProduct.getDescription());
        Assertions.assertEquals(10, saveProduct.getQuantity());
        Assertions.assertEquals(mockCategory, saveProduct.getCategories());
    }

    @Test
    void createProduct_ShouldThrowException_WhenNoCategoryFound() {
        CreateProductRequest request = new CreateProductRequest();
        request.setName("test");
        request.setDescription("product test");
        request.setImageUrl("url test");
        request.setPrice(BigDecimal.valueOf(500));
        request.setCategoryIds(List.of(99L, 100L));
        request.setQuantity(10);
        request.setIsFeatured(false);
        request.setDiscountPercent(BigDecimal.valueOf(0));
        request.setBrand("");
        request.setStatus(ProductStatus.ACTIVE);

        Mockito.when(categoryService.getAllCategoryById(List.of(99L, 100L))).thenReturn(Collections.emptyList());

        Assertions.assertThrows(BusinessException.class, () -> {
            productServiceImpl.createProduct(request);
        }, "No valid category found.");

        Mockito.verify(productRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void updateProduct_ShouldUpdateProduct() {
        Long id = 1L;

        Product product = new Product();
        product.setId(1L);

        UpdateProductRequest request = new UpdateProductRequest();
        request.setName("New name");

        Mockito.when(productRepository.findById(id)).thenReturn(Optional.of(product));

        productServiceImpl.updateProduct(id, request);

        Mockito.verify(productRepository).findById(id);
        Mockito.verify(productMapper).updateProductFromDto(request, product);
        Mockito.verify(productRepository).save(product);

        Assertions.assertEquals("New name", product.getName());
    }

    @Test
    void updateProduct_ShouldThrow_WhenNotFound() {
        Long id = 1L;
        UpdateProductRequest request = new UpdateProductRequest();

        Mockito.when(productRepository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> {
            productServiceImpl.updateProduct(id, request);
        }, "Product not found");
        Mockito.verify(productRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void deleteProduct_ShouldDeleteProduct() {
        Long id = 1L;
        Product product = new Product();

        Mockito.when(productRepository.findById(id)).thenReturn(Optional.of(product));

        productServiceImpl.deleteProduct(id);

        Mockito.verify(productRepository).findById(id);
        Mockito.verify(productRepository).delete(product);
    }

    @Test
    void deleteProduct_ShouldThrow_WhenNotFound() {
        Long id = 1L;

        Mockito.when(productRepository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> {
            productServiceImpl.deleteProduct(id);
        }, "Product not found");

        Mockito.verify(productRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void searchProducts_ShouldReturn_ListProduct() {

        String keyword = "test";

        List<Product> mockProducts = List.of(new Product(), new Product());

        Mockito.when(productRepository.findByNameContainingIgnoreCase(keyword)).thenReturn(mockProducts);

        List<Product> results= productServiceImpl.searchProducts(keyword);

        Mockito.verify(productRepository).findByNameContainingIgnoreCase(keyword);
        Assertions.assertEquals(2, results.size());
        Assertions.assertInstanceOf(Product.class, results.get(0));
    }

    @Test
    void filterByConditions_ShouldReturn_Page() {

        String name = "test";
        Long categoryId = 1L;
        BigDecimal minPrice = new BigDecimal(500);
        BigDecimal maxPrice = new BigDecimal(1000);
        int page = 0;
        int size = 20;

        Pageable pageable = PageRequest.of(page, size);
        List<Product> productList = List.of(new Product(), new Product());
        Page<Product> expectedPage = new PageImpl<>(productList);

        Mockito.when(productRepository.filterByConditions(name, categoryId, minPrice, maxPrice, pageable))
                .thenReturn(expectedPage);

        Page<Product> results = productServiceImpl.filterByConditions(name, categoryId, minPrice, maxPrice, page, size);

        Assertions.assertEquals(2, results.getTotalElements());
        Assertions.assertEquals(1, results.getTotalPages());
        Mockito.verify(productRepository).filterByConditions(name, categoryId, minPrice, maxPrice, pageable);
    }
}