package com.example.service;

import com.example.model.Order;
import com.example.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
@SuppressWarnings("rawtypes")
public class OrderService extends MainService<Order> {

    private final OrderRepository orderRepository;

    // Constructor with Dependency Injection
    @Autowired
    public OrderService(OrderRepository orderRepository) {
        super(orderRepository); // Pass the repository to the parent class (MainService)
        this.orderRepository = orderRepository;
    }

    // Add Order
    public void addOrder(Order order) {
        orderRepository.addOrder(order); // Delegate to the repository's addOrder method
    }

    // Get All Orders
    public ArrayList<Order> getOrders() {
        return orderRepository.getOrders(); // Delegate to the repository
    }

    // Get a Specific Order
    public Order getOrderById(UUID orderId) {
        Order order = orderRepository.getOrderById(orderId); // Delegate to the repository
        if (order == null) {
            throw new IllegalArgumentException("Order not found with ID: " + orderId);
        }
        return order;
    }

    // Delete a Specific Order
    public void deleteOrderById(UUID orderId) throws IllegalArgumentException {
        Order order = orderRepository.getOrderById(orderId); // Check if the order exists
        if (order == null) {
            throw new IllegalArgumentException("Order not found with ID: " + orderId);
        }
        orderRepository.deleteOrderById(orderId); // Delegate to the repository
    }
}