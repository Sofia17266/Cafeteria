package com.cafeteria.app.service;

import com.cafeteria.app.model.Category;
import com.cafeteria.app.model.Subcategory;
import com.cafeteria.app.repository.CategoryRepository;
import com.cafeteria.app.repository.SubcategoryRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final SubcategoryRepository subcategoryRepository;

    // Constructor Injection
    public CategoryService(CategoryRepository categoryRepository, SubcategoryRepository subcategoryRepository) {
        this.categoryRepository = categoryRepository;
        this.subcategoryRepository = subcategoryRepository;
    }

    // Fetch all categories
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // Fetch a category by ID
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Category not found with ID: " + id));
    }

    // Fetch a subcategory by ID
    public Subcategory getSubcategoryById(Long id) {
        return subcategoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Subcategory not found with ID: " + id));
    }
}
