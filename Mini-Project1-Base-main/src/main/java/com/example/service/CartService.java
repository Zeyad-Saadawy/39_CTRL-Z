package com.example.service;

import com.example.model.Cart;
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
}
