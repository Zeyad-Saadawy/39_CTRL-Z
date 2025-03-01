package com.example.model;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class Cart {
    private UUID id;
    private UUID userId;
    private List<Product> products = new ArrayList<>();

    public Cart() {
        this.products = new ArrayList<>();  // Ensures the list isn't null
    }

    public Cart( UUID userId) {
        this.id = UUID.randomUUID(); ;
        this.userId = userId;
        this.products = new ArrayList<>();  // Ensures products are initialized
    }

    public Cart(UUID id, UUID userId, List<Product> products) {
        this.id = (id != null) ? id : UUID.randomUUID();
        this.userId = userId;
        this.products = products != null ? products : new ArrayList<>();  // Avoid null products
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}