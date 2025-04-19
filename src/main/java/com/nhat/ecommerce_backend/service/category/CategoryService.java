package com.nhat.ecommerce_backend.service.category;

import com.nhat.ecommerce_backend.dto.category.CategoryRequest;
import com.nhat.ecommerce_backend.entity.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategory();
    void createCategory(CategoryRequest request);
    void updateCategory(Long id, CategoryRequest request);
    void deleteCategory(Long id);
    List<Category> getAllCategoryById(List<Long> categoryIds);
}
