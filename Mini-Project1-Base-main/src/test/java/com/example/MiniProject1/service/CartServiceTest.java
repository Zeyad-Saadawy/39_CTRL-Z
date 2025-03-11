package com.example.MiniProject1.service;

import com.example.model.Cart;
import com.example.model.Product;
import com.example.repository.CartRepository;
import com.example.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")
public class CartServiceTest {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartService cartService;
    @Value("${spring.application.cartDataPath}")
    private String cartDataPath;

    @BeforeEach
    void setUp() {
        cartRepository = new CartRepository();
        cartService = new CartService(cartRepository);
    }

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(cartRepository, "cartDataPath", "src/main/java/com/example/data/carts.json");
        System.out.println("Injected cartDataPath: " + cartRepository.getDataPath());
    }

    @Test
    void checkIfPropertyIsLoaded() {
        System.out.println("Test Cart Data Path: " + cartDataPath);
    }

    @Test
    void addCart() {  //when user still has no cart

        UUID userId = UUID.randomUUID();
        Cart cart = new Cart(userId);


        Cart result = cartService.addCart(cart);


        assertNotNull(result);
        assertEquals(userId, result.getUserId());
    }


    @Test
    void testAddCart_UserAlreadyHasCart() {

        UUID userId = UUID.randomUUID();
        Cart existingCart = new Cart(userId);


        cartRepository.save(existingCart);


        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cartService.addCart(existingCart);
        });


        assertEquals("User already has a cart!", exception.getMessage());
    }

    @Test
    void testAddCart_StoredCorrectly() {

        // Print the cartDataPath before anything else
        System.out.println("cartDataPath: " + cartDataPath);
        // Arrange
        UUID userId = UUID.randomUUID();
        Cart cart = new Cart(userId);


        // Print before adding the cart
        System.out.println("Adding cart with userId: " + userId);
        Cart addedCart = cartService.addCart(cart);

        // Check if cart was added successfully
        System.out.println("Cart added with ID: " + addedCart.getId());
        Cart retrievedCart = cartService.getCartById(addedCart.getId());

        System.out.println("Retrieved cart: " + retrievedCart);
        assertNotNull(retrievedCart);
        assertEquals(addedCart.getId(), retrievedCart.getId());
        assertEquals(userId, retrievedCart.getUserId());
    }

    @Test
    void testGetCarts_ReturnsAllCarts() {

        UUID user1 = UUID.randomUUID();
        UUID user2 = UUID.randomUUID();

        Cart cart1 = new Cart(user1);
        Cart cart2 = new Cart(user2);

        cartService.addCart(cart1);
        cartService.addCart(cart2);


        List<Cart> carts = cartService.getCarts();

        assertTrue(carts.stream()
                .filter(cart -> cart.getId() != null)  // Ignore null ids
                .anyMatch(cart -> cart.getId().equals(cart1.getId()))
        );

        assertTrue(carts.stream()
                .filter(cart -> cart.getId() != null)  // Ignore null ids
                .anyMatch(cart -> cart.getId().equals(cart1.getId()))
        );
        assertNotNull(carts);
        assertTrue(carts.size() >= 2); // min 2 carts
    }

    @Test
    void testGetCarts_ContainsSpecificCart() {

        UUID userId = UUID.randomUUID();
        Cart expectedCart = new Cart(userId);
        cartService.addCart(expectedCart);


        List<Cart> carts = cartService.getCarts();


        assertNotNull(carts);
        assertTrue(carts.stream().anyMatch(cart -> cart.getUserId().equals(userId)));
    }


    @Test
    void testGetCarts_EnsuresCorrectFormat() {

        List<Cart> carts = cartService.getCarts();


        assertNotNull(carts, "Carts list should not be null");


        for (Cart cart : carts) {
            assertNotNull(cart.getUserId(), "User ID should not be null");
            assertNotNull(cart.getProducts(), "Products list should not be null");
        }
    }

    @Test
    void testGetCartById_ReturnsCorrectCart() {
        // Arrange
        UUID userId = UUID.randomUUID();
        Cart cart = new Cart(userId);
        cartService.addCart(cart);

        // Act
        Cart retrievedCart = cartService.getCartById(cart.getId());

        // Assert
        assertNotNull(retrievedCart);
        assertEquals(cart.getId(), retrievedCart.getId());
        assertEquals(cart.getUserId(), retrievedCart.getUserId());
    }

    @Test
    void testGetCartById_ThrowsException_WhenCartNotFound() {

        UUID randomId = UUID.randomUUID();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            cartService.getCartById(randomId);
        });

        assertEquals("Cart not found", exception.getMessage());
    }

    @Test
    void testGetCartById_ThrowsException_WhenIdIsNull() {

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            cartService.getCartById(null);
        });

        assertEquals("Cart not found", exception.getMessage());
    }

    @Test
    void testGetCartByUserId_ReturnsCorrectCart() {

        UUID userId = UUID.randomUUID();
        Cart cart = new Cart(userId);
        cartService.addCart(cart);


        Cart retrievedCart = cartService.getCartByUserId(userId);


        assertNotNull(retrievedCart);
        assertEquals(cart.getUserId(), retrievedCart.getUserId());
        assertEquals(cart.getId(), retrievedCart.getId());
    }

    @Test
    void testGetCartByUserId_ThrowsException_WhenCartNotFound() {

        UUID randomUserId = UUID.randomUUID();


        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            cartService.getCartByUserId(randomUserId);
        });

        assertEquals("Cart not found", exception.getMessage());
    }

    @Test
    void testGetCartByUserId_ThrowsException_WhenUserIdIsNull() {

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            cartService.getCartByUserId(null);
        });

        assertEquals("Cart not found", exception.getMessage());
    }

    @Test
    void testAddProductToCart_Success() {
        // Arrange
        UUID userId = UUID.randomUUID();
        Cart cart = new Cart(userId);
        cartService.addCart(cart);

        Product product = new Product(UUID.randomUUID(), "Laptop", 1200.0);

        // Act
        cartService.addProductToCart(cart.getId(), product);

        // Assert
        Cart updatedCart = cartService.getCartById(cart.getId());
        assertNotNull(updatedCart);

        boolean productFound = false;
        for (Product p : updatedCart.getProducts()) {
            if (p.getId().equals(product.getId())&& p.getName().equals(product.getName()) && p.getPrice()==(product.getPrice())) {
                productFound = true;
                break;
            }
        }
        assertTrue(productFound);
    }

    @Test
    void testAddProductToCart_CartNotFound() {
        // Arrange
        UUID nonExistentCartId = UUID.randomUUID();
        Product product = new Product(UUID.randomUUID(), "Smartphone", 800.0);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            cartService.addProductToCart(nonExistentCartId, product);
        });

        assertEquals("Cart not found", exception.getMessage());
    }

    @Test
    void testAddProductToCart_NullProduct() {
        // Arrange
        UUID userId = UUID.randomUUID();
        Cart cart = new Cart(userId);
        cartService.addCart(cart);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            cartService.addProductToCart(cart.getId(), null);
        });

        assertEquals("Product cannot be null", exception.getMessage());
    }
    @Test
    void testDeleteProductFromCart_ProductExists() {
        // Arrange
        UUID userId = UUID.randomUUID();
        Cart cart = new Cart(userId);
        cartService.addCart(cart);

        Product product = new Product(UUID.randomUUID(), "Tablet", 300.0);
        cartService.addProductToCart(cart.getId(), product);

        // Act
        cartService.deleteProductFromCart(cart.getId(), product); // Uncomment this line

        // Assert
        Cart updatedCart = cartService.getCartById(cart.getId());
        assertFalse(updatedCart.getProducts().contains(product)); // Check that the product is NOT in the cart
    }
    @Test
    void testDeleteProductFromCart_CartNotFound() {
        // Arrange
        UUID nonExistentCartId = UUID.randomUUID();
        Product product = new Product(UUID.randomUUID(), "Smartphone", 800.0);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            cartService.deleteProductFromCart(nonExistentCartId, product);
        });

        assertEquals("Cart not found", exception.getMessage());
    }
    @Test
    void testDeleteProductFromCart_ProductNotInCart() {
        // Arrange
        UUID userId = UUID.randomUUID();
        Cart cart = new Cart(userId);
        cartService.addCart(cart);

        Product productNotInCart = new Product(UUID.randomUUID(), "Headphones", 200.0);

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            cartService.deleteProductFromCart(cart.getId(), productNotInCart);
        });

        assertTrue(exception.getMessage().equals("Product not found in cart") || exception.getMessage().equals("Cart is empty"));    }
    @Test
    void testDeleteCartById_Success() {
        // Arrange
        UUID userId = UUID.randomUUID();
        Cart cart = new Cart(userId);
        cartService.addCart(cart);

        // Act
        cartService.deleteCartById(cart.getId());

        // Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            cartService.getCartById(cart.getId());
        });

        assertEquals("Cart not found", exception.getMessage());
    }
    @Test
    void testDeleteCartById_CartNotFound() {
        // Arrange
        UUID nonExistentCartId = UUID.randomUUID();

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            cartService.deleteCartById(nonExistentCartId);
        });

        assertEquals("Cart not found", exception.getMessage());
    }
    @Test
    void testDeleteCartById_NullCartId() {
        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            cartService.deleteCartById(null);
        });

        assertEquals("Cart ID cannot be null", exception.getMessage());
    }





}