package com.cafeteria.app.service;

import com.cafeteria.app.model.Product;
import com.cafeteria.app.repository.ProductRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getProductsByIds(List<Long> ids) {
        return productRepository.findByIdIn(ids);
    }

    public Optional<Product> getProductById(Long productId) {
        return productRepository.findById(productId);
    }

    public List<Product> getProductsByCategoryId(Long categoryId) {
        return productRepository.findByCategoryIdAndSubcategoryIsNull(categoryId);
    }

    public List<Product> getProductsBySubcategoryId(Long subcategoryId) {
        return productRepository.findBySubcategoryId(subcategoryId);
    }
}
