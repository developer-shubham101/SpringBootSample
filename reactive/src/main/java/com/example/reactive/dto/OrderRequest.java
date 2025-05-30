package com.example.reactive.dto;

/**
 * Request DTO for order creation.
 * Used to deserialize the incoming JSON request body.
 */
public class OrderRequest {
    public String productId;
    public int quantity;
} 