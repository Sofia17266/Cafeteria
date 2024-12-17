package com.cafeteria.app.controller;

import com.cafeteria.app.model.Cart;
import com.cafeteria.app.model.Product;
import com.cafeteria.app.service.ProductService;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/cart")
public class CartController {
    
    @Autowired
    private ProductService productService;

    @GetMapping
    public String showCart(Model model, HttpSession session) {
        Cart cart = getOrCreateCart(session);

        List<Long> productIds = new ArrayList<>(cart.getItems().keySet());
        List<Product> products = productService.getProductsByIds(productIds);

        double total = 0.0;
        for (Product product : products) {
            int quantity = cart.getItems().get(product.getId());
            total += product.getPrice() * quantity;
        }

        model.addAttribute("cart", cart);
        model.addAttribute("products", products);
        model.addAttribute("total", total);

        return "cart";
    }

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(
        @RequestParam Long productId,
        HttpSession session,
        @RequestHeader("X-Requested-With") String requestedWith
    ) {
        Cart cart = getOrCreateCart(session);
        cart.addProduct(productId);
        session.setAttribute("cart", cart);
        
        if ("XMLHttpRequest".equals(requestedWith)) {
            Map<String, Object> response = new HashMap<>();
            response.put("cartCount", cart.getTotalItems());
            response.put("message", "Producto agregado al carrito");
            return ResponseEntity.ok(response);
        }
        
        return ResponseEntity.ok().build();
    }

    @PostMapping("/remove")
    public ResponseEntity<?> removeFromCart(
        @RequestParam Long productId,
        HttpSession session,
        @RequestHeader("X-Requested-With") String requestedWith
    ) {
        Cart cart = getOrCreateCart(session);
        cart.removeProduct(productId);
        session.setAttribute("cart", cart);

        if ("XMLHttpRequest".equals(requestedWith)) {
            Map<String, Object> response = new HashMap<>();
            response.put("cartCount", cart.getTotalItems());
            response.put("message", "Producto eliminado del carrito");
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.ok().build();
    }

    private Cart getOrCreateCart(HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }
        return cart;
    }
}
