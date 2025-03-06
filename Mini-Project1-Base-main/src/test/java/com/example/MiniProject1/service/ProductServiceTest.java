package com.example.MiniProject1.service;

import com.example.model.Product;
import com.example.repository.ProductRepository;
import com.example.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    public void testAddProduct_Success() {
        Product product = new Product("Laptop", 1000.0);
        when(productRepository.addProduct(product)).thenReturn(product);

        Product result = productService.addProduct(product);

        assertNotNull(result);
        assertEquals(product.getName(), result.getName());
        assertEquals(product.getPrice(), result.getPrice(), 0.001);
        verify(productRepository, times(1)).addProduct(product);
    }

    @Test
    public void testAddProduct_NullValues() {
        Product product = new Product(null, 0.0);
        when(productRepository.addProduct(product)).thenReturn(null);

        Product result = productService.addProduct(product);

        assertNull(result);
        verify(productRepository, times(1)).addProduct(product);
    }

    @Test
    public void testAddProduct_NegativePrice() {
        Product product = new Product("Laptop", -100.0);
        when(productRepository.addProduct(product)).thenReturn(null);

        Product result = productService.addProduct(product);

        assertNull(result);
        verify(productRepository, times(1)).addProduct(product);
    }

    @Test
    public void testGetProducts_Success() {
        ArrayList<Product> products = new ArrayList<>();
        products.add(new Product("Laptop", 1000.0));
        products.add(new Product("Phone", 500.0));

        when(productRepository.getProducts()).thenReturn(products);

        ArrayList<Product> result = productService.getProducts();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(productRepository, times(1)).getProducts();
    }

    @Test
    public void testGetProducts_EmptyRepository() {
        when(productRepository.getProducts()).thenReturn(new ArrayList<>());

        ArrayList<Product> result = productService.getProducts();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(productRepository, times(1)).getProducts();
    }

    @Test
    public void testGetProducts_Exception() {
        when(productRepository.getProducts()).thenThrow(new RuntimeException("File read error"));

        assertThrows(RuntimeException.class, () -> productService.getProducts());
        verify(productRepository, times(1)).getProducts();
    }

    @Test
    public void testGetProductById_Success() {
        UUID productId = UUID.randomUUID();
        Product product = new Product("Laptop", 1000.0);
        product.setId(productId);

        when(productRepository.getProductById(productId)).thenReturn(product);

        Product result = productService.getProductById(productId);

        assertNotNull(result);
        assertEquals(productId, result.getId());
        verify(productRepository, times(1)).getProductById(productId);
    }

    @Test
    public void testGetProductById_NotFound() {
        UUID productId = UUID.randomUUID();
        when(productRepository.getProductById(productId)).thenReturn(null);

        Product result = productService.getProductById(productId);

        assertNull(result);
        verify(productRepository, times(1)).getProductById(productId);
    }

    @Test
    public void testGetProductById_Exception() {
        UUID productId = UUID.randomUUID();
        when(productRepository.getProductById(productId)).thenThrow(new RuntimeException("File read error"));

        assertThrows(RuntimeException.class, () -> productService.getProductById(productId));
        verify(productRepository, times(1)).getProductById(productId);
    }

    @Test
    public void testUpdateProduct_Success() {
        UUID productId = UUID.randomUUID();
        Product product = new Product("Laptop", 1000.0);
        product.setId(productId);

        // Create an updated product
        Product updatedProduct = new Product("New Laptop", 1200.0);
        updatedProduct.setId(productId);

        // Mock the repository to return the existing product before updating
        when(productRepository.getProductById(productId)).thenReturn(product);
        when(productRepository.updateProduct(productId, "New Laptop", 1200.0)).thenReturn(updatedProduct);

        // Call the service method
        Product result = productService.updateProduct(productId, "New Laptop", 1200.0);

        // Verify the results
        assertNotNull(result);
        assertEquals("New Laptop", result.getName());
        assertEquals(1200.0, result.getPrice(), 0.001);
        verify(productRepository, times(1)).getProductById(productId);
        verify(productRepository, times(1)).updateProduct(productId, "New Laptop", 1200.0);
    }
    @Test
    public void testUpdateProduct_NotFound() {
        UUID productId = UUID.randomUUID();

        // Mock repository to return null when trying to fetch the product
        when(productRepository.getProductById(productId)).thenReturn(null);

        // Expect an exception since the product is not found
        assertThrows(IllegalArgumentException.class, () -> productService.updateProduct(productId, "New Laptop", 1200.0));

        verify(productRepository, times(1)).getProductById(productId);
        verify(productRepository, times(0)).updateProduct(any(), any(), anyDouble()); // Ensure updateProduct is never called
    }

    @Test
    public void testUpdateProduct_InvalidData() {
        UUID productId = UUID.randomUUID();
        Product product = new Product("Laptop", 1000.0);
        product.setId(productId);

        // Mock the repository to return an existing product
        when(productRepository.getProductById(productId)).thenReturn(product);

        // Ensure an exception is thrown when updating with an invalid price
        assertThrows(IllegalArgumentException.class, () -> productService.updateProduct(productId, "New Laptop", -1200.0));

        // Verify that getProductById was called once
        verify(productRepository, times(1)).getProductById(productId);

        // Ensure updateProduct is NEVER called due to invalid price
        verify(productRepository, never()).updateProduct(any(), any(), anyDouble());
    }

    @Test
    public void testApplyDiscount_Success() {
        UUID productId = UUID.randomUUID();
        ArrayList<UUID> productIds = new ArrayList<>();
        productIds.add(productId);

        productService.applyDiscount(10.0, productIds);

        verify(productRepository, times(1)).applyDiscount(10.0, productIds);
    }

    @Test
    public void testApplyDiscount_EmptyList() {
        ArrayList<UUID> productIds = new ArrayList<>();

        productService.applyDiscount(10.0, productIds);

        verify(productRepository, times(1)).applyDiscount(10.0, productIds);
    }

    @Test
    public void testApplyDiscount_InvalidDiscount() {
        UUID productId = UUID.randomUUID();
        ArrayList<UUID> productIds = new ArrayList<>();
        productIds.add(productId);

        productService.applyDiscount(-10.0, productIds);

        verify(productRepository, times(1)).applyDiscount(-10.0, productIds);
    }

    @Test
    public void testDeleteProductById_Success() {
        UUID productId = UUID.randomUUID();

        productService.deleteProductById(productId);

        verify(productRepository, times(1)).deleteProductById(productId);
    }

    @Test
    public void testDeleteProductById_NotFound() {
        UUID productId = UUID.randomUUID();

        productService.deleteProductById(productId);

        verify(productRepository, times(1)).deleteProductById(productId);
    }

    @Test
    public void testDeleteProductById_Exception() {
        UUID productId = UUID.randomUUID();
        doThrow(new RuntimeException("File write error")).when(productRepository).deleteProductById(productId);

        assertThrows(RuntimeException.class, () -> productService.deleteProductById(productId));
        verify(productRepository, times(1)).deleteProductById(productId);
    }
}