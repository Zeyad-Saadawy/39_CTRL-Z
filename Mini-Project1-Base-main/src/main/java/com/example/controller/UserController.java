package com.example.controller;

import com.example.model.User;
import com.example.model.Order;
import com.example.model.Cart;
import com.example.model.Product;
import com.example.service.UserService;
import com.example.service.CartService;
import com.example.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final CartService cartService;
    private final ProductService productService;

    @Autowired
    public UserController(UserService userService, CartService cartService, ProductService productService) {
        this.userService = userService;
        this.cartService = cartService;
        this.productService = productService;
    }

    // 8.1.2.1 Add User
    @PostMapping("/")
    public User addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    // 8.1.2.2 Get All Users
    @GetMapping("/")
    public ArrayList<User> getUsers() {
        return userService.getUsers();
    }

    // 8.1.2.3 Get Specific User
    @GetMapping("/{userId}")
    public User getUserById(@PathVariable UUID userId) {
        return userService.getUserById(userId);
    }

    // 8.1.2.4 Get a User’s Orders
    @GetMapping("/{userId}/orders")
    public List<Order> getOrdersByUserId(@PathVariable UUID userId) {
        return userService.getOrdersByUserId(userId);
    }

    // 8.1.2.5 Check Out (Create Order from Cart)
    @PostMapping("/{userId}/checkout")
    public String addOrderToUser(@PathVariable UUID userId) {
        userService.addOrderToUser(userId);
        return "Order added successfully";
    }

    // 8.1.2.6 Remove Order
    @PostMapping("/{userId}/removeOrder")
    public String removeOrderFromUser(@PathVariable UUID userId, @RequestParam UUID orderId) {
        userService.removeOrderFromUser(userId, orderId);
        return "Order removed successfully";
    }

    // 8.1.2.7 Empty Cart
    @DeleteMapping("/{userId}/emptyCart")
    public String emptyCart(@PathVariable UUID userId) {
        userService.emptyCart(userId);
        return "Cart emptied successfully";
    }

    // 8.1.2.8 Add Product To the Cart
    @PutMapping("/addProductToCart")
    public String addProductToCart(@RequestParam UUID userId, @RequestParam UUID productId) {
        // Retrieve the user's cart; if none exists, create a new one.
        Cart cart = cartService.getCartByUserId(userId);
        if (cart == null) {
            cart = cartService.addCart(new Cart(userId));
        }
        // Retrieve the product by its ID.
        Product product = productService.getProductById(productId);
        if (product == null) {
            throw new IllegalArgumentException("Product not found with ID: " + productId);
        }
        // Use the cart's ID along with the product instance.
        cartService.addProductToCart(cart.getId(), product);
        return "Product added to cart successfully";
    }

    // 8.1.2.9 Delete Product from the Cart
    @PutMapping("/deleteProductFromCart")
    public String deleteProductFromCart(@RequestParam UUID userId, @RequestParam UUID productId) {
        // Retrieve the user's cart.
        Cart cart = cartService.getCartByUserId(userId);
        if (cart == null) {
            throw new IllegalArgumentException("Cart not found for user ID: " + userId);
        }
        // Retrieve the product by its ID.
        Product product = productService.getProductById(productId);
        if (product == null) {
            throw new IllegalArgumentException("Product not found with ID: " + productId);
        }
        // Use the cart's ID along with the product instance.
        cartService.deleteProductFromCart(cart.getId(), product);
        return "Product deleted from cart";
    }

    // 8.1.2.10 Delete User
    @DeleteMapping("/delete/{userId}")
    public String deleteUserById(@PathVariable UUID userId) {
        try {
            userService.deleteUserById(userId);
            return "User deleted successfully";
        } catch (IllegalArgumentException e) {
            return "User not found";
        }
    }
}
