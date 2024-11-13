package com.cafeteria.app.repository;

import com.cafeteria.app.model.Category;
import com.cafeteria.app.model.Subcategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
    Optional<Subcategory> findSubcategoryById(Long id);
}
