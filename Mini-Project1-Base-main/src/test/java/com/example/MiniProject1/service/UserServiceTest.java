
package com.example.MiniProject1.service;

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

    @Mock private UserRepository userRepository;
    @Mock private CartService cartService;
    @Mock private OrderService orderService;
    @InjectMocks private UserService userService;

    private UUID userId;
    private User testUser;
    private Product testProduct;
    private Cart testCart;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        testUser = new User(userId, "Test User", new ArrayList<>());
        testProduct = new Product(UUID.randomUUID(), "Test Product", 99.99);
        testCart = new Cart(UUID.randomUUID(), userId, new ArrayList<>());
    }

    // ==================== addUser() Tests ====================
    @Test
    void addUser_GeneratesIdWhenNull() {
        User newUser = new User(null, "New User", new ArrayList<>());

        when(userRepository.addUser(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            u.setId(UUID.randomUUID());
            return u;
        });

        User result = userService.addUser(newUser);
        assertNotNull(result.getId());
        verify(userRepository).addUser(any(User.class));
    }

    @Test
    void addUser_PreservesExistingId() {
        User userWithId = new User(userId, "Existing ID User", new ArrayList<>());
        when(userRepository.addUser(userWithId)).thenReturn(userWithId);

        User result = userService.addUser(userWithId);
        assertEquals(userId, result.getId());
    }

    @Test
    void addUser_HandlesRepositoryErrors() {
        User invalidUser = new User(null, null, null);
        when(userRepository.addUser(any())).thenThrow(new RuntimeException("DB Error"));

        assertThrows(RuntimeException.class, () -> userService.addUser(invalidUser));
    }

    // ==================== getUsers() Tests ====================
    @Test
    void getUsers_ReturnsEmptyListWhenNoUsers() {
        when(userRepository.getUsers()).thenReturn(new ArrayList<>());
        assertTrue(userService.getUsers().isEmpty());
    }

    @Test
    void getUsers_ReturnsMultipleUsers() {
        List<User> mockUsers = Arrays.asList(
                new User(UUID.randomUUID(), "Alice", new ArrayList<>()),
                new User(UUID.randomUUID(), "Bob", new ArrayList<>())
        );
        when(userRepository.getUsers()).thenReturn(new ArrayList<>(mockUsers));

        assertEquals(2, userService.getUsers().size());
    }

    @Test
    void getUsers_HandlesRepositoryExceptions() {
        when(userRepository.getUsers()).thenThrow(new RuntimeException("DB Error"));
        assertThrows(RuntimeException.class, () -> userService.getUsers());
    }

    // ==================== getUserById() Tests ====================
    @Test
    void getUserById_ReturnsCorrectUser() {
        when(userRepository.getUserById(userId)).thenReturn(testUser);
        assertEquals(testUser, userService.getUserById(userId));
    }

    @Test
    void getUserById_ThrowsWhenNotFound() {
        when(userRepository.getUserById(any())).thenThrow(new IllegalArgumentException());
        assertThrows(IllegalArgumentException.class,
                () -> userService.getUserById(UUID.randomUUID()));
    }

    @Test
    void getUserById_ValidatesUUIDFormat() {
        // Mock the behavior of userRepository.getUserById to throw an exception when userId is null
        when(userRepository.getUserById(null)).thenThrow(new IllegalArgumentException("User ID cannot be null"));

        // Act and Assert
        assertThrows(IllegalArgumentException.class,
                () -> userService.getUserById(null));
    }

    // ==================== getOrdersByUserId() Tests ====================
    @Test
    void getOrdersByUserId_ReturnsEmptyOrderList() {
        when(userRepository.getOrdersByUserId(userId)).thenReturn(new ArrayList<>());
        assertTrue(userService.getOrdersByUserId(userId).isEmpty());
    }

    @Test
    void getOrdersByUserId_ReturnsMultipleOrders() {
        List<Order> mockOrders = Arrays.asList(
                new Order(UUID.randomUUID(), userId, 100.0, new ArrayList<>()),
                new Order(UUID.randomUUID(), userId, 200.0, new ArrayList<>())
        );
        when(userRepository.getOrdersByUserId(userId)).thenReturn(mockOrders);
        assertEquals(2, userService.getOrdersByUserId(userId).size());
    }

    @Test
    void getOrdersByUserId_PropagatesRepositoryExceptions() {
        when(userRepository.getOrdersByUserId(any())).thenThrow(new RuntimeException());
        assertThrows(RuntimeException.class,
                () -> userService.getOrdersByUserId(userId));
    }

    // ==================== addOrderToUser() Tests ====================
    @Test
    void addOrderToUser_CreatesValidOrder() {
        testCart.getProducts().add(testProduct);
        when(cartService.getCartByUserId(userId)).thenReturn(testCart);
        when(cartService.calculateTotalPrice(testCart)).thenReturn(99.99);

        userService.addOrderToUser(userId);

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderService).addOrder(orderCaptor.capture());
        assertEquals(userId, orderCaptor.getValue().getUserId());
    }

    @Test
    void addOrderToUser_ThrowsForEmptyCart() {
        when(cartService.getCartByUserId(userId)).thenReturn(testCart);
        assertThrows(IllegalStateException.class,
                () -> userService.addOrderToUser(userId));
    }

    @Test
    void addOrderToUser_ClearsCartAfterOrder() {
        testCart.getProducts().add(testProduct);
        when(cartService.getCartByUserId(userId)).thenReturn(testCart);
        userService.addOrderToUser(userId);
        verify(cartService).emptyCart(userId);
    }

    // ==================== emptyCart() Tests ====================
    @Test
    void emptyCart_DelegatesToService() {
        userService.emptyCart(userId);
        verify(cartService).emptyCart(userId);
    }

    @Test
    void emptyCart_HandlesMissingCart() {
        doThrow(new IllegalArgumentException()).when(cartService).emptyCart(any());
        assertThrows(IllegalArgumentException.class,
                () -> userService.emptyCart(userId));
    }

    @Test
    void emptyCart_ValidatesUserId() {
        // Mock the behavior of cartService.emptyCart to throw an exception when userId is null
        doThrow(new IllegalArgumentException("User ID cannot be null"))
                .when(cartService).emptyCart(null);

        // Act and Assert
        assertThrows(IllegalArgumentException.class,
                () -> userService.emptyCart(null));
    }

    // ==================== removeOrderFromUser() Tests ====================
    @Test
    void removeOrderFromUser_DelegatesToRepository() {
        UUID orderId = UUID.randomUUID();
        userService.removeOrderFromUser(userId, orderId);
        verify(userRepository).removeOrderFromUser(userId, orderId);
    }

    @Test
    void removeOrderFromUser_ThrowsForMissingOrder() {
        doThrow(new IllegalArgumentException()).when(userRepository)
                .removeOrderFromUser(any(), any());
        assertThrows(IllegalArgumentException.class,
                () -> userService.removeOrderFromUser(userId, UUID.randomUUID()));
    }

    @Test
    void removeOrderFromUser_ValidatesParameters() {
        // Mock the behavior of userRepository.removeOrderFromUser to throw an exception when userId is null
        doThrow(new IllegalArgumentException("User ID cannot be null"))
                .when(userRepository).removeOrderFromUser(isNull(), any(UUID.class));

        // Act and Assert
        assertThrows(IllegalArgumentException.class,
                () -> userService.removeOrderFromUser(null, UUID.randomUUID()));
    }

    // ==================== deleteUserById() Tests ====================
    @Test
    void deleteUserById_CleansUpResources() {
        // Act
        userService.deleteUserById(userId);

        // Assert
        verify(userRepository).deleteUserById(userId); // Verify user is deleted
        verifyNoInteractions(cartService); // Ensure cartService is not called
    }

    @Test
    void deleteUserById_HandlesMissingUser() {
        doThrow(new IllegalArgumentException()).when(userRepository).deleteUserById(any());
        assertThrows(IllegalArgumentException.class,
                () -> userService.deleteUserById(userId));
    }

    @Test
    void deleteUserById_IgnoresMissingCart() {
        // No stubbing for cartService.deleteCartByUserId
        userService.deleteUserById(userId); // Should not throw
        verify(userRepository).deleteUserById(userId);
    }
}