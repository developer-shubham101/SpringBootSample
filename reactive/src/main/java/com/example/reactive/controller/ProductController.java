package com.example.reactive.controller;

import com.example.reactive.dto.ProductDetailDTO;
import com.example.reactive.exception.ProductNotFoundException;
import com.example.reactive.model.Product;
import com.example.reactive.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * Reactive REST controller for product operations.
 * 
 * This controller demonstrates reactive web programming using Spring WebFlux:
 * - Returns Flux for multiple items
 * - Returns Mono for single items
 * - Handles reactive streams with backpressure
 * - Provides server-sent events (SSE) endpoints
 * 
 * Key reactive features:
 * 1. Non-blocking HTTP endpoints
 * 2. Reactive error handling
 * 3. Server-sent events streaming
 * 4. Reactive response types (Mono/Flux)
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Returns a Flux of products, optionally filtered by name.
     * 
     * Reactive features:
     * - Returns Flux for streaming multiple products
     * - Non-blocking search operation
     * - Backpressure handling for large result sets
     * 
     * @param name Optional name filter
     * @return Flux of products matching the criteria
     */
    @GetMapping
    public Flux<Product> getAllProducts(@RequestParam(required = false) String name) {
        if (name != null && !name.isEmpty()) {
            return productService.searchProductsByName(name);
        }
        return productService.getAllProducts();
    }

    /**
     * Returns a Mono containing a single product by ID.
     * <p>
     * Reactive features:
     * - Returns Mono for single product
     * - Non-blocking lookup operation
     * - Reactive error handling with defaultIfEmpty
     * 
     * @param id Product ID
     * @return Mono containing ResponseEntity with product or 404
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Product>> getProductById(@PathVariable String id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Returns a Mono containing product details with reviews.
     * 
     * Reactive features:
     * - Returns Mono for single product details
     * - Non-blocking product and review fetching
     * - Reactive error handling with onErrorResume
     * - Type-specific error handling
     * 
     * @param id Product ID
     * @return Mono containing ResponseEntity with product details or error
     */
    @GetMapping("/{id}/details")
    public Mono<ResponseEntity<ProductDetailDTO>> getProductDetails(@PathVariable String id) {
        return productService.getProductWithDetails(id)
                .map(ResponseEntity::ok)
                .onErrorResume(ProductNotFoundException.class, e ->
                        Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)))
                .onErrorResume(ex ->
                        Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null)));
    }

    /**
     * Creates a new product and returns a Mono with the created product.
     * <p>
     * Reactive features:
     * - Returns Mono for single created product
     * - Non-blocking save operation
     * - Reactive error handling
     * 
     * @param product Product to create
     * @return Mono containing the created product
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Product> createProduct(@RequestBody Product product) {
        return productService.createProduct(product);
    }

    /**
     * Streams new product arrivals using Server-Sent Events (SSE).
     * <p>
     * Reactive features:
     * - Returns Flux for streaming multiple products
     * - Server-Sent Events (SSE) endpoint
     * - Non-blocking interval-based generation
     * - Backpressure handling with take operator
     * 
     * @return Flux of new product arrivals
     */
    @GetMapping(value = "/stream/new-arrivals", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Product> streamNewProductArrivals() {
        return Flux.interval(Duration.ofSeconds(5))
                .flatMap(i -> productService.createProduct(
                        new Product("New Arrival #" + i, "Exciting new product", 10.0 + i, 10))
                )
                .take(10);
    }
} 