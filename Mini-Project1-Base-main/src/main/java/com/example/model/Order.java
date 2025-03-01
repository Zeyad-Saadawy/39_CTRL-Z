package com.example.model;

import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.*;
@Component
public class Order {
    private UUID id;
    private UUID userId;
    private double totalPrice;
    private List<Product> products=new ArrayList<>();

    public Object getId() {
        return id;
    }

    public Order(UUID id, UUID userId, double totalPrice, List<Product> products) {
        this.id = id;
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.products = products;
    }
    public Order(){
        this.id = UUID.randomUUID();
        this.userId = UUID.randomUUID();
        this.totalPrice = 0;
        this.products = new ArrayList<>();
    }

    public UUID getUserId() {
        return userId;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}

