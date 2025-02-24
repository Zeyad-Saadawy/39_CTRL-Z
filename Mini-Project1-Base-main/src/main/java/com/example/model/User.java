package com.example.model;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class User {
    private UUID id;
    private String name;
    private List<Order> orders=new ArrayList<>();

    private String nn;
}
