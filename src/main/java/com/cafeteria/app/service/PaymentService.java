package com.cafeteria.app.service;

import com.cafeteria.app.model.Cart;
import com.cafeteria.app.model.PaymentMethod;
import com.cafeteria.app.model.User;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    public boolean processSinpePayment(Cart cart, User user) {
        // Simulate SINPE payment processing
        return true;
    }

    public boolean processApplePay(Cart cart, User user) {
        // Simulate Apple Pay processing
        return true;
    }

    public boolean processCardPayment(Cart cart, User user, PaymentMethod card) {
        // Simulate card payment processing with saved card
        return true;
    }

    public boolean processNewCardPayment(Cart cart, User user, PaymentMethod newCard) {
        // Simulate payment processing with new card details
        return true;
    }
}
