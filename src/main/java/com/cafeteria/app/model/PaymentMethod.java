package com.cafeteria.app.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cardNumber; // Store only the last 4 digits
    private String expiryDate; // Format: MM/YY

    // Do not store CVV in production for security reasons

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // Default constructor
    public PaymentMethod() {}

    // Parameterized constructor
    public PaymentMethod(String cardNumber, String expiryDate, User user) {
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
        this.user = user;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    // Since we're storing only the last 4 digits, we need to handle that
    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        // Extract and store only the last 4 digits
        if (cardNumber != null && cardNumber.length() >= 4) {
            this.cardNumber = cardNumber.substring(cardNumber.length() - 4);
        } else {
            this.cardNumber = cardNumber;
        }
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        // Validate expiry date format (MM/YY)
        this.expiryDate = expiryDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        if (sameAsFormer(user))
            return;
        User oldUser = this.user;
        this.user = user;
        if (oldUser != null)
            oldUser.removePaymentMethod(this);
        if (user != null)
            user.addPaymentMethod(this);
    }

    private boolean sameAsFormer(User newUser) {
        return user == null ? newUser == null : user.equals(newUser);
    }
}
