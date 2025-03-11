package com.example.repository;

import com.example.model.Cart;
import com.example.model.Product;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
//@SuppressWarnings("rawtypes")
public class CartRepository extends MainRepository<Cart>{

    public CartRepository(){  super();
    }


    @Value("${spring.application.cartDataPath}")
    private String cartDataPath;


    @Override
    public String getDataPath() {
        System.out.println("CartRepository getDataPath(): " + cartDataPath);

        return cartDataPath;
    }


    @Override
    protected Class<Cart[]> getArrayType() {
        return Cart[].class;
    }


    public ArrayList<Cart> getCarts() {
        return findAll();
    }


    public Cart addCart(Cart cart) {
        save(cart);
        return cart;
    }

    public Cart getCartById(UUID cartId) {
        System.out.println("Searching for Cart ID: " + cartId);
        return findAll().stream()
                .filter(cart -> cart.getId() != null && cart.getId().equals(cartId))
                .findFirst()
                .orElse(null);  // Return null if not found


    }
    public Cart getCartByUserId(UUID userId) {
        ArrayList<Cart> carts = getCarts();
        if (carts.isEmpty()) {
            System.out.println("No carts found in the system.");
            return null;
        }

        return carts.stream()
                .filter(cart -> cart.getUserId() != null && cart.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
    }

    public void deleteCartById(UUID cartId) {
        ArrayList<Cart> carts = getCarts();
        carts.removeIf(cart -> cart.getId().equals(cartId));
        saveAll(carts);
    }

    public void addProductToCart(UUID cartId, Product product) {
        ArrayList<Cart> carts = getCarts();
        boolean cartFound = false;

        for (Cart cart : carts) {
            if (cart.getId().equals(cartId)) {
                cart.getProducts().add(product);
                cartFound = true;
                break;
            }
        }

        if (!cartFound) {
            throw new IllegalArgumentException("Cart not found with ID: " + cartId);
        }
        overrideData(carts);
    }

    public void deleteProductFromCart(UUID cartId, Product product) {
        ArrayList<Cart> carts = getCarts();
        boolean cartFound = false;
        for (Cart cart : carts) {
            if (cart.getId().equals(cartId)) {
                // Remove the product by comparing IDs (or use equals() if properly overridden)
                cart.getProducts().removeIf(p -> p.getId().equals(product.getId()));
                cartFound = true;
                break;
            }
        }
        if (!cartFound) {
            throw new IllegalArgumentException("Cart not found with ID: " + cartId);
        }
        // Persist the updated list of carts
        overrideData(carts);
    }


}
