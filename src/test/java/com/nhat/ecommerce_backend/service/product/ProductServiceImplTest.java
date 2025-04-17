package com.nhat.ecommerce_backend.service.product;

import com.nhat.ecommerce_backend.dto.product.CreateProductRequest;
import com.nhat.ecommerce_backend.dto.product.UpdateProductRequest;
import com.nhat.ecommerce_backend.entity.Category;
import com.nhat.ecommerce_backend.entity.Product;
import com.nhat.ecommerce_backend.exception.BusinessException;
import com.nhat.ecommerce_backend.model.enums.ProductStatus;
import com.nhat.ecommerce_backend.repository.ProductRepository;
import com.nhat.ecommerce_backend.service.CategoryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryService categoryService;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productServiceImpl;

    @Test
    void getAllProduct_ShouldReturnList() {

        List<Product> mockProducts = List.of(new Product(), new Product());
        Mockito.when(productRepository.findAll()).thenReturn(mockProducts);

        List<Product> results = productRepository.findAll();

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

        Assertions.assertThrows(BusinessException.class, () -> {
            productServiceImpl.getProductById(1L);
        });
    }

    @Test
    void createProduct_ShouldCreateProduct() {

        CreateProductRequest request = new CreateProductRequest();
        request.setName("test");
        request.setDescription("product test");
        request.setImageUrl("url test");
        request.setPrice(BigDecimal.valueOf(500));
        request.setCategoryId(1L);
        request.setQuantity(10);
        request.setIsFeatured(false);
        request.setDiscountPercent(BigDecimal.valueOf(0));
        request.setBrand("");
        request.setStatus(ProductStatus.ACTIVE);

        Category mockCategory = new Category();
        mockCategory.setId(1L);

        Mockito.when(categoryService.getById(1L)).thenReturn(mockCategory);

        productServiceImpl.createProduct(request);

        ArgumentCaptor<Product> productArgumentCaptor = ArgumentCaptor.forClass(Product.class);
        Mockito.verify(productRepository).save(productArgumentCaptor.capture());

        Product saveProduct = productArgumentCaptor.getValue();
        Assertions.assertEquals("test", saveProduct.getName());
        Assertions.assertEquals("product test", saveProduct.getDescription());
        Assertions.assertEquals(10, saveProduct.getQuantity());
        Assertions.assertEquals(mockCategory, saveProduct.getCategory());
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
    }

    @Test
    void updateProduct_ShouldThrow_WhenNotFound() {
        Long id = 1L;
        UpdateProductRequest request = new UpdateProductRequest();

        Mockito.when(productRepository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> {
            productServiceImpl.updateProduct(id, request);
        });
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

        Assertions.assertThrows(BusinessException.class, ()-> {
            productServiceImpl.deleteProduct(id);
        });
    }

    @Test
    void searchProducts_ShouldReturn_ListProduct() {

        String keyword = "test";

        List<Product> mockProducts = List.of(new Product(), new Product());

        Mockito.when(productRepository.findByNameContainingIgnoreCase(keyword)).thenReturn(mockProducts);

        List<Product> results= productServiceImpl.searchProducts(keyword);

        Mockito.verify(productRepository).findByNameContainingIgnoreCase(keyword);
        Assertions.assertEquals(2, results.size());
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
        Mockito.verify(productRepository).filterByConditions(name, categoryId, minPrice, maxPrice, pageable);
    }
}