package com.example.service;

import com.example.model.Product;
import com.example.repository.ProductRepository;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.UUID;

@Service
public class ProductService extends MainService<Product> {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        super(productRepository); // Inject repository into MainService
        this.productRepository = productRepository;
    }

    // Add a new product
    public Product addProduct(Product product) {
        return productRepository.addProduct(product);
    }

    // Get all products
    public ArrayList<Product> getProducts() {
        return productRepository.getProducts();
    }

    // Get a specific product by ID
    public Product getProductById(UUID productId) {
        return productRepository.getProductById(productId);
    }

    // Update a product
    public Product updateProduct(UUID productId, String newName, double newPrice) {
        Product existingProduct = productRepository.getProductById(productId);

        if (existingProduct == null) {
            throw new IllegalArgumentException("Product not found");
        }

        if (newPrice < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }

        return productRepository.updateProduct(productId, newName, newPrice);
    }

    // Apply discount to products
    public void applyDiscount(double discount, ArrayList<UUID> productIds) {
        productRepository.applyDiscount(discount, productIds);
    }

    // Delete a product by ID
    public void deleteProductById(UUID productId) {
        productRepository.deleteProductById(productId);
    }
}