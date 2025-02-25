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
}

