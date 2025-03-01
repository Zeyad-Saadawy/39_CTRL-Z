package com.example.repository;

import com.example.model.Cart;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.UUID;

@Repository
@SuppressWarnings("rawtypes")
public class CartRepository extends MainRepository<Cart>{

    public CartRepository(){
    }


    @Override
    protected String getDataPath() {
        return "carts.json";  // Example for UserRepository
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
        return findAll().stream()
                .filter(cart -> cart.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
    }



}
