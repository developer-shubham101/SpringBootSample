package com.example.reactive.service;

import com.example.reactive.model.Product;
import com.example.reactive.model.ProductOrder;
import com.example.reactive.model.Review;
import com.example.reactive.repository.OrderRepository;
import com.example.reactive.repository.ProductRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SampleDataService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final ObjectMapper objectMapper;

    public SampleDataService(ProductRepository productRepository, OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.objectMapper = new ObjectMapper();
    }

    public Mono<Void> loadSampleData() {
        return Mono.fromCallable(() -> {
            ClassPathResource resource = new ClassPathResource("sample-data.json");
            return objectMapper.readTree(resource.getInputStream());
        })
        .flatMap(root -> {
            // Load Products
            List<Product> products = new ArrayList<>();
            root.get("products").forEach(productNode -> {
                Product product = new Product(
                    productNode.get("name").asText(),
                    productNode.get("description").asText(),
                    productNode.get("price").asDouble(),
                    productNode.get("stockQuantity").asInt()
                );
                products.add(product);
            });

            // Load Orders
            List<ProductOrder> orders = new ArrayList<>();
            root.get("orders").forEach(orderNode -> {
                ProductOrder order = new ProductOrder(
                    orderNode.get("productId").asText(),
                    orderNode.get("quantity").asInt(),
                    orderNode.get("orderStatus").asText()
                );
                orders.add(order);
            });

            // Save Products first, then Orders
            return Flux.fromIterable(products)
                .flatMap(productRepository::save)
                .then()
                .thenMany(Flux.fromIterable(orders))
                .flatMap(orderRepository::save)
                .then();
        })
        .onErrorResume(e -> {
            log.error("Error loading sample data", e);
            return Mono.error(e);
        });
    }

    public Flux<Product> getSampleProducts() {
        return productRepository.findAll();
    }

    public Flux<ProductOrder> getSampleOrders() {
        return orderRepository.findAll();
    }
} 