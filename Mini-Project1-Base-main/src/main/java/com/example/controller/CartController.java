package com.example.controller;

import com.example.model.Cart;
import com.example.model.Product;
import com.example.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @DeleteMapping("/delete/{cartId}")
    public ResponseEntity<String> deleteCartById(@PathVariable UUID cartId) {
        cartService.deleteCartById(cartId);
        return ResponseEntity.ok("Cart deleted successfully");
    }

    @PutMapping("/addProduct/{cartId}")
    public ResponseEntity<String> addProductToCart(@PathVariable UUID cartId, @RequestBody Product product) {
        try {
            cartService.addProductToCart(cartId, product);
            return ResponseEntity.ok("Product added to cart successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
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
