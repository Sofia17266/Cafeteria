package com.cafeteria.app.controller;

import com.cafeteria.app.model.User;
import com.cafeteria.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/login")
    public String showLoginForm() {
        return "auth/login-options";
    }

    @GetMapping("/login/form")
    public String showLoginForm(
        @RequestParam(value = "error", required = false) String error,
        Model model) {
        if (error != null) {
            model.addAttribute("error", "Invalid email or password");
        }
        return "auth/login";
    }
    
    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerCustomer(@ModelAttribute("user") User user, Model model) {
        boolean isRegistered = userService.registerUser(user);
        if (isRegistered) {
            return "redirect:/login/form";
        } else {
            model.addAttribute("error", "Email is already in use");
            return "auth/register";
        }
    }
}
