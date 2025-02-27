package com.example.controller;

import com.example.model.Cart;
import com.example.model.Product;
import com.example.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // Create a new cart for a user
    @PostMapping("/add/{userId}")
    public ResponseEntity<Cart> createCart(@PathVariable UUID userId) {
        Cart newCart = cartService.addCart(userId);
        return ResponseEntity.ok(newCart);
    }

    // Get a user's cart
    @GetMapping("/{userId}")
    public ResponseEntity<Cart> getCartByUserId(@PathVariable UUID userId) {
        Cart cart = cartService.getCartByUserId(userId);
        if (cart != null) {
            return ResponseEntity.ok(cart);
        }
        return ResponseEntity.notFound().build();
    }

    // Add a product to the cart
    @PutMapping("/addProduct")
    public ResponseEntity<String> addProductToCart(@RequestParam UUID userId, @RequestBody Product product) {
        Cart cart = cartService.getCartByUserId(userId);
        if (cart != null) {
            cartService.addProductToCart(userId, product);
            return ResponseEntity.ok("Product added to cart");
        }
        return ResponseEntity.badRequest().body("Cart not found for user: " + userId);
    }

    // Remove a product from the cart
    @PutMapping("/deleteProduct")
    public ResponseEntity<String> deleteProductFromCart(@RequestParam UUID userId, @RequestParam UUID productId) {
        Cart cart = cartService.getCartByUserId(userId);
        if (cart != null) {
            cartService.deleteProductFromCart(userId, productId);
            return ResponseEntity.ok("Product removed from cart");
        }
        return ResponseEntity.badRequest().body("Cart not found for user: " + userId);
    }

    // Delete the entire cart
    @DeleteMapping("/delete/{userId}")
    public String deleteCart(@PathVariable UUID userId) {
        Cart cart = cartService.getCartByUserId(userId);
        if (cart != null) {
            cartService.deleteCart(userId);
            return "Cart deleted successfully";
        }
        return "Cart not found for user: " + userId;
    }
}
