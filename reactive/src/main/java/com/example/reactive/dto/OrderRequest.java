package com.example.reactive.dto;

/**
 * Request DTO for order creation.
 * Used to deserialize the incoming JSON request body.
 */
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

/**
 * Request DTO for order creation.
 * Used to deserialize the incoming JSON request body.
 */
public class OrderRequest {
    @NotBlank(message = "Product ID cannot be blank")
    private String productId;

    @Positive(message = "Quantity must be positive")
    private int quantity;

    // Default constructor for JSON deserialization
    public OrderRequest() {}

    // Parameterized constructor
    public OrderRequest(String productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    // Getters and setters
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}