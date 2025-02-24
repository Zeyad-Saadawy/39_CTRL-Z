package com.example.model;

import org.springframework.stereotype.Component;
import java.util.*;

import java.util.UUID;

@Component
public class User {
    private UUID id;
    private String name;
    private List<Order> orders=new ArrayList<>();

    // getter and setters
    public UUID getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setName(String name) {
        this.name = name;
    }
}
