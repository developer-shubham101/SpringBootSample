package com.example.reactive.controller;

import com.example.reactive.dto.OrderRequest;
import com.example.reactive.exception.InsufficientStockException;
import com.example.reactive.exception.ProductNotFoundException;
import com.example.reactive.model.ProductOrder;
import com.example.reactive.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * Reactive REST controller for order operations.
 * 
 * This controller demonstrates reactive web programming using Spring WebFlux:
 * - Returns Mono for single order operations
 * - Handles reactive error scenarios
 * - Provides non-blocking order processing
 * 
 * Key reactive features:
 * 1. Non-blocking order placement
 * 2. Reactive error handling with type-specific responses
 * 3. Transactional order processing
 * 4. Reactive response types (Mono)
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Places a new order and returns a Mono with the created order.
     * 
     * Reactive features:
     * - Returns Mono for single order creation
     * - Non-blocking order processing
     * - Reactive error handling with onErrorResume
     * - Type-specific error responses:
     *   * 404 for ProductNotFoundException
     *   * 409 for InsufficientStockException
     *   * 500 for other exceptions
     * 
     * @param orderRequest Order request containing productId and quantity
     * @return Mono containing ResponseEntity with created order or error
     */
    @PostMapping
    public Mono<ResponseEntity<ProductOrder>> placeOrder(@RequestBody OrderRequest orderRequest) {
        return orderService.placeOrder(orderRequest.productId, orderRequest.quantity)
                .map(order -> ResponseEntity.status(HttpStatus.CREATED).body(order))
                .onErrorResume(ProductNotFoundException.class, e ->
                        Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)))
                .onErrorResume(InsufficientStockException.class, e ->
                        Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(null)))
                .onErrorResume(e ->
                        Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null)));
    }
}