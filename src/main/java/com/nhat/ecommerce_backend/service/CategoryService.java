package com.nhat.ecommerce_backend.service;

import com.nhat.ecommerce_backend.dto.CategoryRequest;
import com.nhat.ecommerce_backend.entity.Category;
import com.nhat.ecommerce_backend.exception.BusinessException;
import com.nhat.ecommerce_backend.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    public Category getById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Category not found"));
    }

    public Category createCategory(CategoryRequest request) {
        if (categoryRepository.findByName(request.getName()) != null) {
            throw new RuntimeException("Category đã tồn tại !");
        }

        Category category = new Category();
        category.setName(request.getName());
        category.setCreateAt(LocalDateTime.now());
        return categoryRepository.save(category);
    }

    public Category updateCategory(Long id, CategoryRequest request) {
        var category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy category !"));

        category.setName(request.getName());
        return categoryRepository.save(category);
    }

    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy category để xóa !"));

        categoryRepository.delete(category);
    }
}
