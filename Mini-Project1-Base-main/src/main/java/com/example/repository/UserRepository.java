package com.example.repository;

import com.example.model.Order;
import com.example.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class UserRepository {
    public User addUser(User user) {
        return null;
    }

    public ArrayList<User> getUsers() {
        return null;
    }

    public User getUserById(UUID userId) {
        return null;
    }

    public List<Order> getOrdersByUserId(UUID userId) {
        return null;
    }

    public void addOrderToUser(UUID userId, Order order) {
    }

    public void removeOrderFromUser(UUID userId, UUID orderId) {
    }

    public void deleteUserById(UUID userId) {
    }
}
