package com.example.repository;

import com.example.model.Order;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.UUID;

@Repository
@SuppressWarnings("rawtypes")
public class OrderRepository extends MainRepository<Order> {

    public OrderRepository() {
        super();
    }

    @Value("${spring.application.orderDataPath}")
    private String orderDataPath; // Injected from application.properties

    @Override
    public String getDataPath() {
        return orderDataPath;
    }
    @Override
    protected Class<Order[]> getArrayType() {
        return Order[].class; // Array type for deserialization
    }

    // Add Order
    public void addOrder(Order order) {

        ArrayList<Order> orders = findAll();
        orders.add(order);
        saveAll(orders);
    }

    // Get All Orders
    public ArrayList<Order> getOrders() {
        return findAll(); // Use the findAll method from MainRepository
    }

    // Get a Specific Order
    public Order getOrderById(UUID orderId) {
        ArrayList<Order> orders = findAll();
        for (Order order : orders) {
            if (order.getId().equals(orderId)) {
                return order;
            }
        }
        return null; // Return null if the order is not found
    }

    // Delete a Specific Order
    public void deleteOrderById(UUID orderId) {
        ArrayList<Order> orders = findAll();
        orders.removeIf(order -> order.getId().equals(orderId)); // Remove the order with the given ID
        overrideData(orders); // Save the updated list back to the JSON file
    }
}