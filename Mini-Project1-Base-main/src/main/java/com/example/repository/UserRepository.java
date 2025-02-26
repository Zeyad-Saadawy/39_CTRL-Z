package com.example.repository;

import com.example.model.Order;
import com.example.model.User;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class UserRepository extends MainRepository<User> {

    @Override
    protected String getDataPath() {
        return "src/main/java/com/example/data/users.json";
    }

    @Override
    protected Class<User[]> getArrayType() {
        return User[].class;
    }

    // Custom methods
    public ArrayList<User> getUsers() {
        return findAll();
    }

    public User getUserById(UUID userId) {
        return findAll().stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
    }

    public User addUser(User user) {
        if (user.getId() == null) {
            user.setId(UUID.randomUUID());
        }
        save(user); // Inherited from MainRepository
        return user;
    }

    public List<Order> getOrdersByUserId(UUID userId) {
        User user = getUserById(userId);
        return user.getOrders();
    }

    public void addOrderToUser(UUID userId, Order order) {
        User user = getUserById(userId); // Throws exception if user not found with ID
        user.getOrders().add(order);
        updateUser(user);
    }

    public void removeOrderFromUser(UUID userId, UUID orderId) {
        User user = getUserById(userId); // Throws exception if user not found
        boolean removed = user.getOrders().removeIf(order -> order.getId().equals(orderId));

        if (!removed) {
            throw new IllegalArgumentException("Order not found with ID: " + orderId);
        }
        updateUser(user);
    }

    public void deleteUserById(UUID userId) {
        ArrayList<User> users = findAll();
        users.removeIf(user -> user.getId().equals(userId));
        overrideData(users);
    }

    private void updateUser(User user) {
        ArrayList<User> users = findAll();
        users.replaceAll(u -> u.getId().equals(user.getId()) ? user : u);
        overrideData(users);
    }
}