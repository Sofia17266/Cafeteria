package com.cafeteria.app.controller;

import com.cafeteria.app.model.Order;
import com.cafeteria.app.model.User;
import com.cafeteria.app.service.EmailService;
import com.cafeteria.app.service.OrderService;
import com.cafeteria.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    @GetMapping("/orderConfirmation")
    public String orderConfirmation(@RequestParam Long orderId, Model model) {
        Optional<Order> orderOpt = orderService.findById(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            model.addAttribute("order", order);

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (isAuthenticated(auth)) {
                User user = userService.findByEmail(auth.getName());
                model.addAttribute("user", user);
            } else {
                model.addAttribute("user", null);
            }

            return "order-confirmation";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/orderConfirmation/sendInvoice")
    public String sendInvoice(
            @RequestParam Long orderId,
            @RequestParam(required = false) String email,
            Model model) {

        Optional<Order> orderOpt = orderService.findById(orderId);
        if (!orderOpt.isPresent()) {
            return "redirect:/";
        }

        Order order = orderOpt.get();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String recipientEmail = null;

        if (isAuthenticated(auth)) {
            User user = userService.findByEmail(auth.getName());
            recipientEmail = user.getEmail();
        } else {
            // Guest user
            if (email == null || email.isEmpty()) {
                model.addAttribute("order", order);
                model.addAttribute("error", "Por favor, proporcione una dirección de correo electrónico.");
                return "orderConfirmation";
            }
            recipientEmail = email;
        }

        // Enviar el correo
        boolean emailSent = emailService.sendInvoiceEmail(order, recipientEmail);

        if (emailSent) {
            model.addAttribute("successMessage", "La factura ha sido enviada a su correo electrónico.");
        } else {
            model.addAttribute("error", "Ocurrió un error al enviar la factura. Por favor, inténtelo de nuevo.");
        }

        model.addAttribute("order", order);

        if (isAuthenticated(auth)) {
            User user = userService.findByEmail(auth.getName());
            model.addAttribute("user", user);
        } else {
            model.addAttribute("user", null);
        }

        return "orderConfirmation";
    }

    private boolean isAuthenticated(Authentication auth) {
        return auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken);
    }
}
