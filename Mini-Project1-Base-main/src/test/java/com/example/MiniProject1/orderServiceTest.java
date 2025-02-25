package com.example.MiniProject1;

import com.example.model.Order;
import com.example.model.Product;
import com.example.repository.OrderRepository;
import com.example.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class orderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    // Test cases for addOrder
    @Test
    void addOrder_Success() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        List<Product> products = new ArrayList<>();
        Order order = new Order(orderId, userId, 100.0, products); // Use the Order constructor
        doNothing().when(orderRepository).addOrder(order);

        // Act
        orderService.addOrder(order);

        // Assert
        verify(orderRepository, times(1)).addOrder(order); // Verify repository method was called
    }

    @Test
    void addOrder_NullOrder_ThrowsException() {
        // Arrange
        Order order = null;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> orderService.addOrder(order));
    }

    @Test
    void addOrder_RepositoryThrowsException() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        List<Product> products = new ArrayList<>();
        Order order = new Order(orderId, userId, 100.0, products); // Use the Order constructor
        doThrow(new RuntimeException("Repository error")).when(orderRepository).addOrder(order);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> orderService.addOrder(order));
    }

    // Test cases for getOrders
    @Test
    void getOrders_Success() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        List<Product> products = new ArrayList<>();
        Order order = new Order(orderId, userId, 100.0, products); // Use the Order constructor

        ArrayList<Order> expectedOrders = new ArrayList<>();
        expectedOrders.add(order);
        when(orderRepository.getOrders()).thenReturn(expectedOrders);

        // Act
        ArrayList<Order> actualOrders = orderService.getOrders();

        // Assert
        assertEquals(expectedOrders, actualOrders); // Verify the returned list matches
        verify(orderRepository, times(1)).getOrders(); // Verify repository method was called
    }

    @Test
    void getOrders_EmptyList() {
        // Arrange
        when(orderRepository.getOrders()).thenReturn(new ArrayList<>());

        // Act
        ArrayList<Order> actualOrders = orderService.getOrders();

        // Assert
        assertTrue(actualOrders.isEmpty()); // Verify the list is empty
    }

    @Test
    void getOrders_RepositoryThrowsException() {
        // Arrange
        when(orderRepository.getOrders()).thenThrow(new RuntimeException("Repository error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> orderService.getOrders());
    }

    // Test cases for getOrderById
    @Test
    void getOrderById_Success() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        List<Product> products = new ArrayList<>();
        Order expectedOrder = new Order(orderId, userId, 100.0, products); // Use the Order constructor
        when(orderRepository.getOrderById(orderId)).thenReturn(expectedOrder);

        // Act
        Order actualOrder = orderService.getOrderById(orderId);

        // Assert
        assertEquals(expectedOrder, actualOrder); // Verify the returned order matches
        verify(orderRepository, times(1)).getOrderById(orderId); // Verify repository method was called
    }

    @Test
    void getOrderById_OrderNotFound_ThrowsException() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        when(orderRepository.getOrderById(orderId)).thenReturn(null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> orderService.getOrderById(orderId));
    }

    @Test
    void getOrderById_RepositoryThrowsException() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        when(orderRepository.getOrderById(orderId)).thenThrow(new RuntimeException("Repository error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> orderService.getOrderById(orderId));
    }

    // Test cases for deleteOrderById
    @Test
    void deleteOrderById_Success() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        List<Product> products = new ArrayList<>();
        Order order = new Order(orderId, userId, 100.0, products); // Use the Order constructor
        when(orderRepository.getOrderById(orderId)).thenReturn(order);
        doNothing().when(orderRepository).deleteOrderById(orderId);

        // Act
        orderService.deleteOrderById(orderId);

        // Assert
        verify(orderRepository, times(1)).deleteOrderById(orderId); // Verify repository method was called
    }

    @Test
    void deleteOrderById_OrderNotFound_ThrowsException() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        when(orderRepository.getOrderById(orderId)).thenReturn(null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> orderService.deleteOrderById(orderId));
    }

    @Test
    void deleteOrderById_RepositoryThrowsException() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        List<Product> products = new ArrayList<>();
        Order order = new Order(orderId, userId, 100.0, products); // Use the Order constructor
        when(orderRepository.getOrderById(orderId)).thenReturn(order);
        doThrow(new RuntimeException("Repository error")).when(orderRepository).deleteOrderById(orderId);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> orderService.deleteOrderById(orderId));
    }
}