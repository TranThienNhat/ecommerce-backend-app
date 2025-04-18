package com.nhat.ecommerce_backend.service;

import com.nhat.ecommerce_backend.dto.category.CategoryRequest;
import com.nhat.ecommerce_backend.entity.Category;
import com.nhat.ecommerce_backend.exception.BusinessException;
import com.nhat.ecommerce_backend.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    public void createCategory(CategoryRequest request) {
        if (categoryRepository.findByName(request.getName()) != null) {
            throw new BusinessException("category already exists");
        }

        Category category = new Category();
        category.setName(request.getName());
        categoryRepository.save(category);
    }

    public void updateCategory(Long id, CategoryRequest request) {
        var category = categoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Category not found"));

        category.setName(request.getName());
        categoryRepository.save(category);
    }

    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                        .orElseThrow(() -> new BusinessException("Category not found"));

        categoryRepository.delete(category);
    }

    public List<Category> getAllCategoryById(List<Long> categoryIds) {
        return categoryRepository.findAllById(categoryIds);
    }
}
