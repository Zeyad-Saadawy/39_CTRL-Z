package com.example.service;

import com.example.model.Cart;
import org.springframework.stereotype.Service;

@Service
public class CartService {
    // CartService.java
    public double calculateTotalPrice(Cart cart) {
        if (cart == null || cart.getProducts() == null || cart.getProducts().isEmpty()) {
            return 0.0;
        }

        return cart.getProducts().stream()
                .mapToDouble(Product::getPrice)
                .sum();
    }

}
