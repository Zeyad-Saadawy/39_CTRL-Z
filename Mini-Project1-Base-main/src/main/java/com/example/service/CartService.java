package com.example.service;

import com.example.model.Cart;
import com.example.model.Product;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CartService {
    public Cart getCartByUserId(UUID userId) {
        return null;
    }

    public void emptyCart(UUID userId) {

    }

    public void deleteCartByUserId(UUID userId) {
    }

    public double calculateTotalPrice(Cart cart) {
        if(cart == null || cart.getProducts().isEmpty()) {
            return 0.0;
        }

        return cart.getProducts().stream()
                .mapToDouble(Product::getPrice)
                .sum();
    }
}
