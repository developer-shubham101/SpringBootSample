package com.example.reactive.service;

import com.example.reactive.exception.InsufficientStockException;
import com.example.reactive.exception.ProductNotFoundException;
import com.example.reactive.exception.StockUpdateException;
import com.example.reactive.model.Product;
import com.example.reactive.model.ProductOrder;
import com.example.reactive.repository.OrderRepository;
import com.example.reactive.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

/**
 * Service class demonstrating reactive order processing using Project Reactor.
 * Handles order placement with stock validation and updates in a reactive manner.
 * Uses Mono for single-item operations and demonstrates complex reactive flows.
 */
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    /**
     * Places an order for a product with reactive transaction handling.
     * Demonstrates several reactive programming concepts:
     * 1. Error handling with switchIfEmpty
     * 2. Conditional processing with flatMap
     * 3. Chaining operations with then and defer
     * 4. Side effects with doOnSuccess and doOnError
     * 5. Reactive transaction management
     */
    @Transactional
    public Mono<ProductOrder> placeOrder(String productId, int quantity) {
        // findById returns a Mono that emits the found product or completes empty
        return productRepository.findById(productId)
                // switchIfEmpty transforms an empty Mono into an error Mono with the specified exception
                .switchIfEmpty(Mono.error(new ProductNotFoundException("Product to order not found: " + productId)))
                // flatMap transforms the product Mono and returns a new Mono with the order
                .flatMap(product -> {
                    // Returns an error Mono if stock is insufficient
                    if (product.getStockQuantity() < quantity) {
                        return Mono.error(new InsufficientStockException("Not enough stock for product: " + product.getName()));
                    }
                    
                    // Updates the product's stock quantity
                    product.setStockQuantity(product.getStockQuantity() - quantity);
                    // save returns a Mono that emits the saved product
                    return productRepository.save(product)
                            // then ignores the product result and chains to the next operation
                            .then(Mono.defer(() -> {
                                // defer creates a new Mono for each subscription, ensuring fresh order creation
                                ProductOrder order = new ProductOrder(productId, quantity, "PENDING");
                                // save returns a Mono that emits the saved order
                                return orderRepository.save(order);
                            }));
                })
                // doOnSuccess executes when the Mono completes successfully, emitting the order
                .doOnSuccess(order -> System.out.println("Order placed successfully: " + order.getId()))
                // doOnError executes when the Mono emits an error of type StockUpdateException
                .doOnError(StockUpdateException.class, e -> System.err.println("Stock update failed during order: " + e.getMessage()))
                // doOnError executes when the Mono emits an error of type InsufficientStockException
                .doOnError(InsufficientStockException.class, e -> System.err.println("Order failed due to insufficient stock: " + e.getMessage()));
    }
} 