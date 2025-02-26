package com.example.MiniProject1;

import com.example.model.User;
import com.example.model.Order;
import com.example.model.Cart;
import com.example.model.Product;
import com.example.repository.UserRepository;
import com.example.service.CartService;
import com.example.service.OrderService;
import com.example.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartService cartService;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private UserService userService;

    private UUID userId;
    private User testUser;
    private Product testProduct;
    private Cart testCart;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();

        testUser = new User();
        testUser.setId(userId);
        testUser.setName("Test User");
        testUser.setOrders(new ArrayList<>());


        testProduct = new Product();
        testProduct.setId(UUID.randomUUID());
        testProduct.setName("Test Product");
        testProduct.setPrice(99.99);


        testCart = new Cart();
        testCart.setId(UUID.randomUUID());
        testCart.setUserId(userId);
        testCart.setProducts(new ArrayList<>());
    }

    // === addUser() Tests ===

    @Test
    void addUser_GeneratesIdWhenNull() {
        User newUser = new User();
        newUser.setName("New User");
        newUser.setOrders(new ArrayList<>());

        when(userRepository.addUser(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            u.setId(UUID.randomUUID());
            return u;
        });

        User result = userService.addUser(newUser);
        assertNotNull(result.getId(), "Should generate UUID when null");
        assertEquals("New User", result.getName(), "Name should match input");
        verify(userRepository, times(1)).addUser(any(User.class));
    }

    @Test
    void addUser_PersistsUserWithExistingId() {
        User userWithId = new User();
        userWithId.setId(userId);
        userWithId.setName("Existing ID User");
        userWithId.setOrders(new ArrayList<>());

        when(userRepository.addUser(userWithId)).thenReturn(userWithId);

        User result = userService.addUser(userWithId);
        assertEquals(userId, result.getId(), "Should preserve existing ID");
        verify(userRepository, times(1)).addUser(userWithId);
    }

    // ... [Keep other addUser() tests unchanged] ...

    // === getUsers() Tests ===

    @Test
    void getUsers_ReturnsAllUsers() {
        // Create users using setters
        User user1 = new User();
        user1.setId(UUID.randomUUID());
        user1.setName("Alice");

        User user2 = new User();
        user2.setId(UUID.randomUUID());
        user2.setName("Bob");

        when(userRepository.getUsers()).thenReturn(new ArrayList<>(Arrays.asList(user1, user2)));

        ArrayList<User> result = userService.getUsers();
        assertEquals(2, result.size(), "Should return all users");
        verify(userRepository, times(1)).getUsers();
    }

    // ... [Keep other getUsers() tests unchanged] ...

    // === addOrderToUser() Tests ===

    @Test
    void addOrderToUser_CreatesValidOrder() {
        // Add product to cart using setters
        testCart.getProducts().add(testProduct);
        when(cartService.getCartByUserId(userId)).thenReturn(testCart);

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);

        userService.addOrderToUser(userId);

        verify(orderService).addOrder(orderCaptor.capture());
        Order createdOrder = orderCaptor.getValue();

        // Verify order details using getters
        assertEquals(userId, createdOrder.getUserId(), "Order should belong to correct user");
        assertEquals(99.99, createdOrder.getTotalPrice(), 0.001, "Order total should match cart total");
        assertEquals(1, createdOrder.getProducts().size(), "Should contain cart products");

        verify(cartService, times(1)).emptyCart(userId);
    }

    @Test
    void addOrderToUser_ThrowsForEmptyCart() {
        // Initialize empty cart via setters
        Cart emptyCart = new Cart();
        emptyCart.setId(UUID.randomUUID());
        emptyCart.setUserId(userId);
        emptyCart.setProducts(new ArrayList<>());

        when(cartService.getCartByUserId(userId)).thenReturn(emptyCart);

        Exception ex = assertThrows(IllegalStateException.class, () ->
                        userService.addOrderToUser(userId),
                "Should reject checkout for empty cart"
        );
        assertTrue(ex.getMessage().contains("Cannot checkout empty cart"));
    }

    // === deleteUserById() Tests ===

    @Test
    void deleteUserById_CleansUpResources() {
        // Initialize user via setters
        User user = new User();
        user.setId(userId);
        user.setName("Test User");

        doNothing().when(userRepository).deleteUserById(userId);
        doNothing().when(cartService).deleteCartByUserId(userId);

        userService.deleteUserById(userId);
        verify(userRepository, times(1)).deleteUserById(userId);
        verify(cartService, times(1)).deleteCartByUserId(userId);
    }

    // === Other Test Methods ===
    // [Keep all other tests with similar modifications to use setters]

    @Test
    void removeOrderFromUser_DelegatesToRepository() {
        UUID orderId = UUID.randomUUID();

        // Initialize order via setters
        Order order = new Order();
        order.setId(orderId);
        order.setUserId(userId);

        doNothing().when(userRepository).removeOrderFromUser(userId, orderId);

        userService.removeOrderFromUser(userId, orderId);
        verify(userRepository, times(1)).removeOrderFromUser(userId, orderId);
    }

    @Test
    void emptyCart_DelegatesToService() {
        // Initialize cart via setters
        Cart cart = new Cart();
        cart.setId(UUID.randomUUID());
        cart.setUserId(userId);

        doNothing().when(cartService).emptyCart(userId);

        userService.emptyCart(userId);
        verify(cartService, times(1)).emptyCart(userId);
    }
}