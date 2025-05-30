package com.example.reactive.service;

import com.example.reactive.model.Review;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Service
public class ReviewServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(ReviewServiceClient.class);
    private final WebClient reviewServiceWebClient;

    public ReviewServiceClient(@Qualifier("reviewWebClient") WebClient reviewServiceWebClient) {
        this.reviewServiceWebClient = reviewServiceWebClient;
    }

    public Flux<Review> getReviewsForProduct(String productId) {
        return reviewServiceWebClient.get()
                .uri("/reviews/{productId}", productId)
                .retrieve()
                .bodyToFlux(Review.class)
                .onErrorResume(ex -> {
                    logger.error("Error fetching reviews for product {}: {}", productId, ex.getMessage());
                    return Flux.empty();
                })
                .timeout(Duration.ofSeconds(2), Flux.empty());
    }
} 