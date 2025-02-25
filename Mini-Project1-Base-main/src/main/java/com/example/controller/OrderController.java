package com.example.controller;

import com.example.model.Order;
import com.example.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.UUID;

@RestController
@RequestMapping("/order")
public class OrderController {

    // Dependency Injection
    private final OrderService orderService;

    // Constructor with Dependency Injection
    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    //  Add Order
    @PostMapping("/")
    public void addOrder(@RequestBody Order order) {
        orderService.addOrder(order); // Delegate to the service layer
    }

    // Get a Specific Order
    @GetMapping("/{orderId}")
    public Order getOrderById(@PathVariable UUID orderId) {
        return orderService.getOrderById(orderId); // Delegate to the service layer
    }

    //  Get All Orders
    @GetMapping("/")
    public ArrayList<Order> getOrders() {
        return orderService.getOrders(); // Delegate to the service layer
    }

    //  Delete a Specific Order
    @DeleteMapping("/delete/{orderId}")
    public String deleteOrderById(@PathVariable UUID orderId) {
        orderService.deleteOrderById(orderId); // Delegate to the service layer
        return "Order with ID " + orderId + " has been deleted successfully.";
    }
}