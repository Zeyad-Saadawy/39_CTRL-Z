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
}

