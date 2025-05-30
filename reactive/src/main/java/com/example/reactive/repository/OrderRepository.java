package com.example.reactive.repository;

import com.example.reactive.model.ProductOrder;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * Reactive MongoDB repository for ProductOrder entities.
 * 
 * This repository extends ReactiveMongoRepository to provide:
 * - Non-blocking database operations for orders
 * - Reactive CRUD operations
 * - Transactional order management
 * 
 * Key reactive features:
 * 1. All methods return Mono or Flux
 * 2. Non-blocking database operations
 * 3. Backpressure handling
 * 4. Reactive transaction support
 * 
 * Inherited reactive methods from ReactiveMongoRepository:
 * - save(Mono<ProductOrder>): Saves an order reactively
 * - findById(String): Finds an order by ID reactively
 * - findAll(): Returns all orders as a reactive stream
 * - deleteById(String): Deletes an order reactively
 */
public interface OrderRepository extends ReactiveMongoRepository<ProductOrder, String> {
} 