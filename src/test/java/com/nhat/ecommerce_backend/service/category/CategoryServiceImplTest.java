package com.nhat.ecommerce_backend.service.category;

import com.nhat.ecommerce_backend.dto.category.CategoryRequest;
import com.nhat.ecommerce_backend.entity.Category;
import com.nhat.ecommerce_backend.exception.BusinessException;
import com.nhat.ecommerce_backend.repository.CategoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @InjectMocks
    private CategoryServiceImpl categoryServiceImpl;

    @Mock
    private CategoryRepository categoryRepository;

    private CategoryRequest request;

    @BeforeEach
    void setUp() {
        request = new CategoryRequest();
        request.setName("test");
        System.out.println("Setup test data");
    }

    @AfterEach
    void tearDown() {
        request = null;
        System.out.println("Clean up after test");
    }

    @Test
    void createCategory_ShouldCreate_Category() {

        Mockito.when(categoryRepository.findByName(request.getName())).thenReturn(null);

        categoryServiceImpl.createCategory(request);

        Mockito.verify(categoryRepository).save(any());
    }

    @Test
    void createCategory_ShouldThrow_CategoryExist() {
        Mockito.when(categoryRepository.findByName(request.getName()))
                .thenReturn(new Category());

        BusinessException exception = Assertions.assertThrows(BusinessException.class, () -> {
            categoryServiceImpl.createCategory(request);
        });
        Assertions.assertEquals("category already exists", exception.getMessage());

        Mockito.verify(categoryRepository, Mockito.never()).save(any());
    }

    @Test
    void updateCategory_ShouldUpdate_Category() {

        Mockito.when(categoryRepository.findById(1L)).thenReturn(Optional.of(new Category()));

        categoryServiceImpl.updateCategory(1L,request);

        Mockito.verify(categoryRepository).save(Mockito.argThat(category ->
                "test".equals(category.getName())
        ));
    }

    @Test
    void updateCategory_ShouldThrow_NotFoundCategory() {

        Mockito.when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        BusinessException exception = Assertions.assertThrows(BusinessException.class, () -> {
            categoryServiceImpl.updateCategory(1L, request);
        });
        Assertions.assertEquals("Category not found", exception.getMessage());

        Mockito.verify(categoryRepository, Mockito.never()).save(any());
    }

    @Test
    void deleteCategory_ShouldDelete_Category() {

        Category categoryDelete = new Category();
        categoryDelete.setId(1L);

        Mockito.when(categoryRepository.findById(1L)).thenReturn(Optional.of(categoryDelete));

        categoryServiceImpl.deleteCategory(1L);

        Mockito.verify(categoryRepository, Mockito.times(1)).delete(categoryDelete);
    }

    @Test
    void deleteCategory_ShouldThrow_NotFoundCategory() {

        Mockito.when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        BusinessException exception = Assertions.assertThrows(BusinessException.class, () -> {
            categoryServiceImpl.deleteCategory(1L);
        });

        Assertions.assertEquals("Category not found", exception.getMessage());

        Mockito.verify(categoryRepository, Mockito.never()).save(any());
        Mockito.verify(categoryRepository, Mockito.never()).delete(any());
    }


    @Test
    void getAllCategoryById_ShouldReturn_ListCategory() {

        List<Category> categories = List.of(new Category(), new Category());

        Mockito.when(categoryRepository.findAllById(List.of(1L,2L))).thenReturn(categories);

        List<Category> results = categoryServiceImpl.getAllCategoryById(List.of(1L, 2L));

        Assertions.assertEquals(2, results.size());
    }
}