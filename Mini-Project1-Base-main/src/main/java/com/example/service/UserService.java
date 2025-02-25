package com.example.service;

import com.example.model.User;
import com.example.model.Order;
import com.example.model.Cart;
import com.example.model.Product;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserService extends MainService<User> {

    private final UserRepository userRepository;
    private final CartService cartService;
    private final OrderService orderService;

    @Autowired
    public UserService(UserRepository userRepository,
                       CartService cartService,
                       OrderService orderService) {
        super(userRepository);
        this.userRepository = userRepository;
        this.cartService = cartService;
        this.orderService = orderService;
    }

    // 7.2.2.1 Add New User
    public User addUser(User user) {
        return userRepository.addUser(user); // Uses repository's UUID generation
    }

    // 7.2.2.2 Get the Users
    public ArrayList<User> getUsers() {
        return userRepository.getUsers();
    }

    // 7.2.2.3 Get a Specific User
    public User getUserById(UUID userId) {
        return userRepository.getUserById(userId);
    }

    // 7.2.2.4 Get the User's Orders
    public List<Order> getOrdersByUserId(UUID userId) {
        return userRepository.getOrdersByUserId(userId);
    }

    // 7.2.2.5 Add a New Order (Checkout)
    public void addOrderToUser(UUID userId) {
        Cart cart = cartService.getCartByUserId(userId);
        if (cart == null || cart.getProducts().isEmpty()) {
            throw new IllegalStateException("Cart is empty");
        }

        // Calculate total price
        double totalPrice = cart.getProducts().stream()
                .mapToDouble(Product::getPrice)
                .sum();

        // Create order
        Order order = new Order(
                UUID.randomUUID(),
                userId,
                totalPrice,
                new ArrayList<>(cart.getProducts())
        );

        // Persist order
        orderService.addOrder(order);

        // Update user and clear cart
        userRepository.addOrderToUser(userId, order);
        cartService.emptyCart(userId);
    }

    // 7.2.2.6 Empty Cart
    public void emptyCart(UUID userId) {
        cartService.emptyCart(userId);
    }

    // 7.2.2.7 Remove Order
    public void removeOrderFromUser(UUID userId, UUID orderId) {
        userRepository.removeOrderFromUser(userId, orderId);
    }

    // 7.2.2.8 Delete the User
    public void deleteUserById(UUID userId) {
        userRepository.deleteUserById(userId);
        cartService.deleteCartByUserId(userId);
    }
}