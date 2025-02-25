package com.example.model;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class Product {
    private UUID id;
    private String name;
    private double price;


    // Default constructor
    public Product() {
        this.id = UUID.randomUUID(); // Auto-generate ID
    }

    // Parameterized constructor
    public Product(String name, double price) {
        this.id = UUID.randomUUID(); // Auto-generate ID
        this.name = name;
        this.price = price;
    }

    // Copy constructor
    public Product(Product product) {
        this.id = product.id;
        this.name = product.name;
        this.price = product.price;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}

