package com.cafeteria.app.model;

import java.util.HashMap;
import java.util.Map;

public class Cart {
    private Map<Long, Integer> items = new HashMap<>();

    public void addProduct(Long productId) {
        items.merge(productId, 1, Integer::sum);
    }

    public void removeProduct(Long productId) {
        items.remove(productId);
    }

    public Map<Long, Integer> getItems() {
        return items;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public int getTotalItems() {
        return items.values().stream().mapToInt(Integer::intValue).sum();
    }
}
