package com.example.reactive.service;

import com.example.reactive.dto.ProductDetailDTO;
import com.example.reactive.exception.ProductNotFoundException;
import com.example.reactive.model.Product;
import com.example.reactive.model.Review;
import com.example.reactive.repository.ProductRepository;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

/**
 * Service class demonstrating various reactive programming concepts using Project Reactor.
 * Uses Mono for single-item operations and Flux for multiple-item operations.
 */
@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
    private final ProductRepository productRepository;
    private final ReviewServiceClient reviewServiceClient;
    private final Validator validator;

    public ProductService(ProductRepository productRepository, 
                         ReviewServiceClient reviewServiceClient,
                         Validator validator) {
        this.productRepository = productRepository;
        this.reviewServiceClient = reviewServiceClient;
        this.validator = validator;
    }

    /**
     * Returns a Flux of all products.
     * Flux is used here because we're dealing with multiple items (a stream of products).
     * findAll() returns a reactive stream of all products in the database.
     */
    public Flux<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Returns a Mono containing a single product by ID.
     * Mono is used here because we're dealing with a single item (one product).
     * findById() returns a reactive stream that will emit at most one product.
     */
    public Mono<Product> getProductById(String productId) {
        return productRepository.findById(productId);
    }

    /**
     * Returns a Flux of products matching the search name.
     * Uses case-insensitive partial matching.
     * Returns a reactive stream of multiple products that match the search criteria.
     */
    public Flux<Product> searchProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    /**
     * Combines product details with its reviews using reactive operators.
     * Demonstrates several reactive programming concepts:
     * 1. Error handling with switchIfEmpty
     * 2. Combining multiple reactive streams with Mono.zip
     * 3. Transforming data with map operator
     */
    public Mono<ProductDetailDTO> getProductWithDetails(String productId) {
        // Get product details from database
        Mono<Product> productMono = productRepository.findById(productId)
                .switchIfEmpty(Mono.error(new ProductNotFoundException("Product not found with id: " + productId)));

        // Get reviews for the product from review service
        Mono<List<Review>> reviewsMono = reviewServiceClient.getReviewsForProduct(productId)
                .collectList();

        // Mono.zip combines multiple Mono publishers into a single Mono containing a tuple of their results
        // In this case, it combines the product and reviews into a single Mono<Tuple2<Product, List<Review>>>
        return Mono.zip(productMono, reviewsMono)
                // map transforms the tuple into a ProductDetailDTO
                // tuple.getT1() gets the Product from the first Mono
                // tuple.getT2() gets the List<Review> from the second Mono
                .map(tuple -> new ProductDetailDTO(tuple.getT1(), tuple.getT2()));
    }

    /**
     * Creates a new product using reactive operators.
     * Demonstrates several reactive programming concepts:
     * 1. Creating a Mono from a value using Mono.just
     * 2. Transforming data with map operator
     * 3. Side effects with doOnSuccess and doOnError
     * 4. Chaining operations with flatMap
     * 5. Input validation before processing
     */
    public Mono<Product> createProduct(Product product) {
        return Mono.just(product)
                // Validate the product before processing
                .flatMap(p -> {
                    Set<jakarta.validation.ConstraintViolation<Product>> violations = validator.validate(p);
                    if (!violations.isEmpty()) {
                        String errorMessage = violations.stream()
                                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                                .reduce((a, b) -> a + "; " + b)
                                .orElse("Validation failed");
                        logger.error("Product validation failed: {}", errorMessage);
                        return Mono.error(new IllegalArgumentException(errorMessage));
                    }
                    return Mono.just(p);
                })
                // map operator transforms the product before saving
                .map(p -> {
                    logger.info("Performing pre-save logic for: {}", p.getName());
                    if (p.getDescription() == null || p.getDescription().isEmpty()) {
                        p.setDescription("No description available.");
                    }
                    return p;
                })
                // flatMap is used here because save() returns a Mono
                // It flattens the nested Mono structure
                .flatMap(productRepository::save)
                // doOnSuccess is a side effect operator that executes when the operation succeeds
                .doOnSuccess(p -> logger.info("Product created: {}", p.getName()))
                // doOnError is a side effect operator that executes when the operation fails
                .doOnError(e -> logger.error("Error creating product: {}", e.getMessage()));
    }
} 