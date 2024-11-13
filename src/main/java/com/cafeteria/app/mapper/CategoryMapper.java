package com.cafeteria.app.mapper;

import com.cafeteria.app.dto.CategoryDTO;
import com.cafeteria.app.dto.ProductDTO;
import com.cafeteria.app.model.Category;
import com.cafeteria.app.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDTO toCategoryDTO(Category category);

    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "subcategory.id", target = "subcategoryId")
    ProductDTO toProductDTO(Product product);
}
