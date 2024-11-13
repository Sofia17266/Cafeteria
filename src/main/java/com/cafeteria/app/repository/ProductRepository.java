package com.cafeteria.app.repository;

import com.cafeteria.app.model.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByName(String name);
    List<Product> findByCategoryId(Long categoryId);
    List<Product> findByIdIn(List<Long> ids);
    List<Product> findByCategoryIdAndSubcategoryIsNull(Long categoryId);
    List<Product> findBySubcategoryId(Long subcategoryId);
}
