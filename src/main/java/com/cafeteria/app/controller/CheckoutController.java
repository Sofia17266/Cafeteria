package com.cafeteria.app.controller;

import com.cafeteria.app.dto.CardForm;
import com.cafeteria.app.model.*;
import com.cafeteria.app.service.*;
import jakarta.servlet.http.HttpSession;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PaymentMethodService paymentMethodService;

    @GetMapping
    public String showCheckoutForm(Model model, HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            model.addAttribute("error", "Tu carrito está vacío.");
            return "checkout";
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = null;
        if (isAuthenticated(auth)) {
            user = userService.findByEmail(auth.getName());
            if (user == null) {
                user = new User();
            }
            model.addAttribute("user", user);

            // Fetch user's saved payment methods
            List<PaymentMethod> paymentMethods = paymentMethodService.getPaymentMethodsByUser(user);
            model.addAttribute("paymentMethods", paymentMethods);
        } else {
            model.addAttribute("user", new User());
            model.addAttribute("paymentMethods", Collections.emptyList());
        }

        model.addAttribute("cart", cart);
        return "checkout";
    }

    @PostMapping
    public String processCheckout(
            @RequestParam("paymentMethod") String paymentMethodParam,
            HttpSession session,
            Model model) {

        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            model.addAttribute("error", "Tu carrito está vacío.");
            return "checkout";
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!isAuthenticated(auth)) {
            model.addAttribute("error", "Debe iniciar sesión para continuar.");
            return "checkout";
        }

        User user = userService.findByEmail(auth.getName());
        boolean paymentSuccess = false;

        if ("new-card".equals(paymentMethodParam)) {
            // Redirect to payment form for adding a new card
            return "redirect:/checkout/payment-form";
        } else if (paymentMethodParam.startsWith("card-")) {
            Long cardId = Long.valueOf(paymentMethodParam.substring(5));
            PaymentMethod card = paymentMethodService.getPaymentMethodById(cardId);
            if (card != null && card.getUser().getId().equals(user.getId())) {
                paymentSuccess = paymentService.processCardPayment(cart, user, card);
            } else {
                model.addAttribute("error", "Método de pago no válido.");
                return "checkout";
            }
        } else {
            // Handle other payment methods
            switch (paymentMethodParam) {
                case "sinpe" -> paymentSuccess = paymentService.processSinpePayment(cart, user);
                case "cash" -> paymentSuccess = true; // Assume success for cash
                case "apple-pay" -> paymentSuccess = paymentService.processApplePay(cart, user);
                default -> {
                    model.addAttribute("error", "Método de pago no válido.");
                    return "checkout";
                }
            }
        }

        if (!paymentSuccess) {
            model.addAttribute("error", "El pago ha fallado.");
            return "checkout";
        }

        Order order = orderService.createOrder(user, cart);
        session.removeAttribute("cart");

        return "redirect:/orderConfirmation?orderId=" + order.getId();
    }

    @GetMapping("/payment-form")
    public String showPaymentForm(Model model) {
        model.addAttribute("cardForm", new CardForm());
        return "payment-form";
    }

    @PostMapping("/process-payment")
    public String processPayment(
            @ModelAttribute("cardForm") CardForm cardForm,
            HttpSession session,
            Model model) {

        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            model.addAttribute("error", "Tu carrito está vacío.");
            return "payment-form";
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!isAuthenticated(auth)) {
            model.addAttribute("error", "Debe iniciar sesión para continuar.");
            return "payment-form";
        }

        User user = userService.findByEmail(auth.getName());

        PaymentMethod newCard = new PaymentMethod();
        newCard.setCardNumber(cardForm.getCardNumber());
        newCard.setExpiryDate(cardForm.getExpiryDate());
        newCard.setUser(user);

        boolean paymentSuccess = paymentService.processNewCardPayment(cart, user, newCard);

        if (!paymentSuccess) {
            model.addAttribute("error", "El pago ha fallado.");
            return "payment-form";
        }

        if (cardForm.isSaveCard()) {
            paymentMethodService.savePaymentMethod(newCard);
        }

        Order order = orderService.createOrder(user, cart);
        session.removeAttribute("cart");

        return "redirect:/orderConfirmation?orderId=" + order.getId();
    }

    private boolean isAuthenticated(Authentication auth) {
        return auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken);
    }
}
