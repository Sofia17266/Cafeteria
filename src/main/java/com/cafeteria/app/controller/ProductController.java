package com.cafeteria.app.controller;

import com.cafeteria.app.dto.CategoryDTO;
import com.cafeteria.app.dto.ProductDTO;
import com.cafeteria.app.model.Category;
import com.cafeteria.app.model.Product;
import com.cafeteria.app.model.Subcategory;
import com.cafeteria.app.service.CategoryService;
import com.cafeteria.app.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final CategoryService categoryService;
    private final ProductService productService;
   

    @Autowired
    public ProductController(CategoryService categoryService, ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }

    @GetMapping
    public String listCategories(Model model) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "products/categories";
    }

    @GetMapping("/category/{id}")
    public String showCategory(@PathVariable Long id, Model model) {
        Category category = categoryService.getCategoryById(id);
        model.addAttribute("category", category);
        
        if (category.getSubcategories() == null || category.getSubcategories().isEmpty()) {
            // No subcategories, list products directly under the category
            List<Product> products = productService.getProductsByCategoryId(id);
            model.addAttribute("products", products);
            return "products/product-list";
        } else {
            // Has subcategories, display them
            List<Subcategory> subcategories = category.getSubcategories();
            model.addAttribute("subcategories", subcategories);
            return "products/subcategories";
        }
    }
    
    
    
    
    
    // save and delete for DTO category entity
    @GetMapping("/elimina/category/{id}")
    public String elimina(Category category)
    {
        categoryService.delete(category);
                return "redirect:/products";            
    }
    // save category method, careful with this path
    @PostMapping("/guardar")
    public String Guardarcategory(CategoryDTO category)
    {
        categoryService.save(category);
        return "redirect:/products";
    }
    
    
    //save and delete for DTO product entity
    @GetMapping("/eliminar/{id}")
    public String eliminar(Product products)
    {
        productService.delete(products);
        return "redirect:/products";
    }
    
    @PostMapping("/Guardar")
    public String Guardar(ProductDTO product)
    {
        productService.save(product);
        return "redirect:/products";
                 
    }
    
    
    

    @GetMapping("/subcategory/{id}")
    public String showSubcategory(@PathVariable Long id, Model model) {
        Subcategory subcategory = categoryService.getSubcategoryById(id);
        model.addAttribute("subcategory", subcategory);
        List<Product> products = productService.getProductsBySubcategoryId(id);
        model.addAttribute("products", products);
        return "products/product-list";
    }
}
