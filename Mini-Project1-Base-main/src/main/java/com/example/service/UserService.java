package com.example.service;

import com.example.model.User;
import com.example.model.Order;
import com.example.model.Cart;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final CartService cartService;
    private final OrderService orderService;

    @Autowired
    public UserService(UserRepository userRepository, CartService cartService, OrderService orderService) {
        this.userRepository = userRepository;
        this.cartService = cartService;
        this.orderService = orderService;
    }

    public User addUser(User user) {
        return userRepository.addUser(user);
    }

    public ArrayList<User> getUsers() {
        return userRepository.getUsers();
    }

    public User getUserById(UUID userId) {
        return userRepository.getUserById(userId);
    }

    public List<Order> getOrdersByUserId(UUID userId) {
        return userRepository.getOrdersByUserId(userId);
    }

    public void addOrderToUser(UUID userId) {
        Cart cart = cartService.getCartByUserId(userId);
        if (cart == null || cart.getProducts().isEmpty()) {
            throw new IllegalArgumentException("Cart is empty or does not exist");
        }

        double totalPrice = cart.getProducts().stream()
                .mapToDouble(Product::getPrice)
                .sum();

        Order order = new Order(UUID.randomUUID(), userId, totalPrice, cart.getProducts());
        orderService.addOrder(order);
        userRepository.addOrderToUser(userId, order);
        cartService.emptyCart(userId);
    }

    public void emptyCart(UUID userId) {
        cartService.emptyCart(userId);
    }

    public void removeOrderFromUser(UUID userId, UUID orderId) {
        userRepository.removeOrderFromUser(userId, orderId);
    }

    public void deleteUserById(UUID userId) {
        userRepository.deleteUserById(userId);
        cartService.deleteCartByUserId(userId);
    }
}