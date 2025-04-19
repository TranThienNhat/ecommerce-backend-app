package com.nhat.ecommerce_backend.service.category;

import com.nhat.ecommerce_backend.dto.category.CategoryRequest;
import com.nhat.ecommerce_backend.entity.Category;
import com.nhat.ecommerce_backend.exception.BusinessException;
import com.nhat.ecommerce_backend.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    public void createCategory(CategoryRequest request) {
        log.info("Create category: {}0", request.getName());
        if (categoryRepository.findByName(request.getName()) != null) {
            throw new BusinessException("category already exists");
        }

        Category category = new Category();
        category.setName(request.getName());
        categoryRepository.save(category);
        log.info("Category created successfully: {}", category.getName());
    }

    public void updateCategory(Long id, CategoryRequest request) {
        log.info("Update category with id: {}", id);
        var category = categoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Category not found"));

        category.setName(request.getName());
        categoryRepository.save(category);
        log.info("Category update successful: {}", category.getId());
    }

    @Transactional
    public void deleteCategory(Long id) {
        log.info("Delete category with id: {}", id);
        Category category = categoryRepository.findById(id)
                        .orElseThrow(() -> new BusinessException("Category not found"));

        categoryRepository.delete(category);
        log.info("Category deleted successfully: {}", category.getName());
    }

    public List<Category> getAllCategoryById(List<Long> categoryIds) {
        log.info("Get all categories with Ids: {}", categoryIds);
        return categoryRepository.findAllById(categoryIds);
    }
}
