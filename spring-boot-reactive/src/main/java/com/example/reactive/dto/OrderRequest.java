package com.example.reactive.dto;

/**
 * Request DTO for order creation.
 * Used to deserialize the incoming JSON request body.
 */
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

/**
 * Request DTO for order creation.
 * Used to deserialize the incoming JSON request body.
 */
@Setter
@Getter
public class OrderRequest {
    // Getters and setters
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

}