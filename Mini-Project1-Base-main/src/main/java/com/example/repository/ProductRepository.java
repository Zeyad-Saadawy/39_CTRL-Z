package com.example.repository;

import com.example.model.Product;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class ProductRepository extends MainRepository<Product> {

    public ProductRepository() {
        super();
    }

    @Value("${spring.application.productDataPath}")
    private String productDataPath; // Injected from application.properties

    @Override
    public String getDataPath() {
        return productDataPath;
    }
    @Override
    protected Class<Product[]> getArrayType() {
        return Product[].class; // Array type for deserialization
    }

    // Add a new product
    public Product addProduct(Product product) {
        ArrayList<Product> products = findAll();
        products.add(product);
        saveAll(products);
        return product;
    }

    // Get all products
    public ArrayList<Product> getProducts() {
        return findAll();
    }

    // Get a specific product by ID
    public Product getProductById(UUID productId) {
        return findAll().stream()
                .filter(product -> product.getId().equals(productId))
                .findFirst()
                .orElse(null);
    }

    // Update a product
    public Product updateProduct(UUID productId, String newName, double newPrice) {
        ArrayList<Product> products = findAll();
        for (Product product : products) {
            if (product.getId().equals(productId)) {
                product.setName(newName); // Update the name
                product.setPrice(newPrice); // Update the price
                saveAll(products); // Save the updated list
                return product; // Return the updated product
            }
        }
        return null; // Product not found
    }

    // Apply discount to products
    public void applyDiscount(double discount, ArrayList<UUID> productIds) {
        ArrayList<Product> products = findAll();
        for (Product product : products) {
            if (productIds.contains(product.getId())) {
                double discountedPrice = product.getPrice() * (1 - discount / 100);
                product.setPrice(discountedPrice);
            }
        }
        saveAll(products);
    }

    // Delete a product by ID
    public void deleteProductById(UUID productId) {
        ArrayList<Product> products = findAll();
        products.removeIf(product -> product.getId().equals(productId));
        saveAll(products);
    }
}