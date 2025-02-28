package com.example.service;

import com.example.model.Cart;
import com.example.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.model.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CartService {
    private final CartRepository cartRepository;

    @Autowired
    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }
    public double calculateTotalPrice(Cart cart) {
        if (cart == null || cart.getProducts() == null || cart.getProducts().isEmpty()) {
            return 0.0;
        }

        return cart.getProducts().stream()
                .mapToDouble(Product::getPrice)
                .sum();
    }

    public Cart addCart(Cart cart) {
        Cart existingCart = cartRepository.getCartByUserId(cart.getUserId());

        if (existingCart != null) { // Check if cart already exists
            throw new IllegalArgumentException("User already has a cart!");
        }

        return cartRepository.addCart(cart);
    }

    public List<Cart> getCarts() {
        return cartRepository.findAll();
    }

    public Cart getCartById(UUID cartId) {
        Cart cart = cartRepository.getCartById(cartId);

        if (cart == null) {
            throw new IllegalArgumentException("Cart not found ");
        }

        return cart;
    }

    public Cart getCartByUserId(UUID userId) {
        Cart cart = cartRepository.getCartByUserId(userId);

        if (cart == null) {
            throw new IllegalArgumentException("Cart not found ");
        }

        return cart;
    }


    public void emptyCart(UUID userId) {
    }

    public void deleteCartByUserId(UUID userId) {
    }
}
