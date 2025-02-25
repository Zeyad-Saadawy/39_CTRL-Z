package com.example.model;

import org.springframework.stereotype.Component;
import java.util.*;

import java.util.UUID;

@Component
public class User {
    private UUID id;
    private String name;
    private List<Order> orders=new ArrayList<>();

    // 3 Constructors
    // 1. No-args constructor
    public User() {}

    // 2. Constructor with all fields (id, name, orders)
    public User(UUID id, String name, List<Order> orders) {
        this.id = id;
        this.name = name;
        this.orders = orders;
    }

    // 3. Constructor without "id"
    public User(String name, List<Order> orders) {
        this.name = name;
        this.orders = orders;
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<Order> getOrders() { return orders; }
    public void setOrders(List<Order> orders) { this.orders = orders; }

}
