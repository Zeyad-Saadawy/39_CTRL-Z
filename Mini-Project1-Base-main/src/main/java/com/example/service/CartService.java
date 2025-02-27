package com.example.service;

import com.example.model.Cart;
import com.example.model.Product;
import com.example.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
public class CartService {

    private final CartRepository cartRepository;

    @Autowired
    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    // Get all carts
    public ArrayList<Cart> getAllCarts() {
        return null;
    }

    // Get cart by ID
    public Cart getCartById(UUID cartId) {

        return null;
    }

    // Add a new cart
    public Cart addCart(UUID userId) {
        return null;
    }

    // Get cart for a specific user
    public Cart getCartByUserId(UUID userId) {
        return cartRepository.getCarts().stream()
                .filter(cart -> cart.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
    }

    // Add a product to a user's cart
    public void addProductToCart(UUID userId, Product product) {
        Cart cart = getCartByUserId(userId);
        if (cart != null) {
            cartRepository.addProductToCart(cart.getId(), product);
        } else {
            throw new IllegalArgumentException("Cart not found for user: " + userId);
        }
    }

    // Remove a product from a user's cart
    public void deleteProductFromCart(UUID userId, UUID productId) {
        Cart cart = getCartByUserId(userId);
        if (cart != null) {
            cartRepository.deleteProductFromCart(cart.getId(), productId);
        } else {
            throw new IllegalArgumentException("Cart not found for user: " + userId);
        }
    }

    // Delete an entire cart by user ID
    public void deleteCart(UUID userId) {
        Cart cart = getCartByUserId(userId);
        if (cart != null) {
            cartRepository.deleteCartById(cart.getId());
        } else {
            throw new IllegalArgumentException("Cart not found for user: " + userId);
        }
    }
}
