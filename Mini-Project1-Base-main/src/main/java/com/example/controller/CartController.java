package com.example.controller;

import com.example.model.Cart;
import com.example.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.UUID;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/")
    public Cart addCart(@RequestBody Cart cart) {
        return cartService.addCart(cart);
    }

    @GetMapping("/")
    public ArrayList<Cart> getCarts() {
        return new ArrayList<>(cartService.getCarts()); // Convert List to ArrayList explicitly
    }


    @GetMapping("/{cartId}")
    public Cart getCartById(@PathVariable UUID cartId) {
        return cartService.getCartById(cartId);
    }


    @GetMapping("/user/{userId}")
    public Cart getCartByUserId(@PathVariable UUID userId) {
        return cartService.getCartByUserId(userId);
    }
}
