package com.example.service;

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
