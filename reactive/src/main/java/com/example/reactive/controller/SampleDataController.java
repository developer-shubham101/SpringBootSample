package com.example.reactive.controller;

import com.example.reactive.model.Product;
import com.example.reactive.model.ProductOrder;
import com.example.reactive.service.SampleDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/sample-data")
@RequiredArgsConstructor
public class SampleDataController {

    private final SampleDataService sampleDataService;

    @PostMapping("/load")
    public Mono<ResponseEntity<String>> loadSampleData() {
        return sampleDataService.loadSampleData()
            .then(Mono.just(ResponseEntity.ok("Sample data loaded successfully")))
            .onErrorResume(e -> Mono.just(ResponseEntity.internalServerError()
                .body("Error loading sample data: " + e.getMessage())));
    }

    @GetMapping("/products")
    public Flux<Product> getSampleProducts() {
        return sampleDataService.getSampleProducts();
    }

    @GetMapping("/orders")
    public Flux<ProductOrder> getSampleOrders() {
        return sampleDataService.getSampleOrders();
    }
} 