package com.cafeteria.app.service;

import com.cafeteria.app.model.*;
import com.cafeteria.app.repository.OrderRepository;
import com.cafeteria.app.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    public Order createOrder(User user, Cart cart) {
        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setCustomer(user);

        List<OrderItem> orderItems = new ArrayList<>();
        double totalPrice = 0.0;

        for (Map.Entry<Long, Integer> entry : cart.getItems().entrySet()) {
            Long productId = entry.getKey();
            Integer quantity = entry.getValue();
            Product product = productRepository.findById(productId).orElse(null);

            if (product != null) {
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(order);
                orderItem.setProduct(product);
                orderItem.setQuantity(quantity);
                orderItems.add(orderItem);

                totalPrice += product.getPrice() * quantity;
            }
        }

        order.setItems(orderItems);
        order.setTotalPrice(totalPrice);

        return orderRepository.save(order);
    }

    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }
}
