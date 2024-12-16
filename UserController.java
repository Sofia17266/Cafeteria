
package com.cafeteria.app.controller;

import com.cafeteria.app.dto.UserDTO;
import com.cafeteria.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
@RequestMapping("/profile")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public String showUserProfile(Model model, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();

        UserDTO userDTO = userService.getUserDTOByEmail(email);
        model.addAttribute("user", userDTO);
        return "account/profile"; 
    }


    @PostMapping
public String updateProfile(@ModelAttribute("user") UserDTO userDTO,
                            Model model,
                            Authentication authentication) {
    String currentEmail = authentication.getName(); 

  
    boolean success = userService.updateUserProfile(currentEmail, userDTO);
    model.addAttribute("success", success);


    if (!currentEmail.equals(userDTO.getEmail())) {
        
        return "redirect:/login?emailChanged=true";
    }

    
    UserDTO updatedUser = userService.getUserDTOByEmail(currentEmail);
    model.addAttribute("user", updatedUser);

    return "account/profile";
}
}