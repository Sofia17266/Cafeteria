package com.cafeteria.app.repository;

import com.cafeteria.app.model.Category;
import com.cafeteria.app.model.Subcategory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubcategoryRepository extends JpaRepository<Subcategory, Long> {
    Optional<Subcategory> findByNameAndCategory(String name, Category category);
    List<Subcategory> findByCategoryId(Long categoryId);
}
