package com.example.reactive.repository;

import com.example.reactive.model.Product;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Reactive MongoDB repository for Product entities.
 * 
 * This repository extends ReactiveMongoRepository to provide:
 * - Non-blocking database operations
 * - Reactive CRUD operations
 * - Custom reactive query methods
 * 
 * Key reactive features:
 * 1. All methods return Mono or Flux
 * 2. Non-blocking database operations
 * 3. Backpressure handling
 * 4. Reactive query execution
 */
public interface ProductRepository extends ReactiveMongoRepository<Product, String> {
    /**
     * Finds products by name using case-insensitive partial matching.
     * 
     * Reactive features:
     * - Returns Flux for multiple matching products
     * - Non-blocking query execution
     * - Backpressure handling for large result sets
     * 
     * @param name Product name to search for
     * @return Flux of matching products
     */
    Flux<Product> findByNameContainingIgnoreCase(String name);
    
    /**
     * Finds a product by ID with stock quantity check.
     * 
     * Reactive features:
     * - Returns Mono for single product
     * - Non-blocking query execution
     * - Combines ID lookup with stock validation
     * 
     * @param id Product ID
     * @param quantity Minimum required stock quantity
     * @return Mono containing the product if found and has sufficient stock
     */
    Mono<Product> findByIdAndStockQuantityGreaterThanEqual(String id, int quantity);
} 