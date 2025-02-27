package com.example.repository;

import com.example.model.Cart;
import com.example.model.Product;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.UUID;


@Repository
@SuppressWarnings("rawtypes")
public class CartRepository extends MainRepository<Cart> {

    @Value("${spring.application.cartDataPath}")
    private String cartDataPath; // Injected from application.properties

    @Override
    public String getDataPath() {
        return cartDataPath;
    }

    @Override
    public Class< Cart[]> getArrayType() {
        return Cart[].class;
    }


    public CartRepository() {}


    public ArrayList<Cart> getCarts() {
        return findAll();
    }

    public Cart getCartById(UUID cartId) {
        return getCarts().stream()
                .filter(cart -> cart.getId().equals(cartId))
                .findFirst()
                .orElse(null);
    }

    public void addProductToCart(UUID cartId, Product product) {
        ArrayList<Cart> carts = getCarts();
        for (Cart cart : carts) {
            if (cart.getId().equals(cartId)) {
                cart.getProducts().add(product);
                save(cart);
                return;
            }
        }
    }

    public void deleteProductFromCart(UUID cartId, UUID productId) {
        ArrayList<Cart> carts = getCarts();
        for (Cart cart : carts) {
            if (cart.getId().equals(cartId)) {
                cart.getProducts().removeIf(product -> product.getId().equals(productId));
                save(cart);
                return;
            }
        }
    }

    public void deleteCartById(UUID cartId) {
        ArrayList<Cart> carts = getCarts();
        carts.removeIf(cart -> cart.getId().equals(cartId));
        saveAll(carts);
    }
}
