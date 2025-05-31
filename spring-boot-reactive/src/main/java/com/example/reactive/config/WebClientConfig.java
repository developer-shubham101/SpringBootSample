package com.example.reactive.config;

import io.micrometer.tracing.Tracer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.TimeoutException;

/**
 * Configuration class for reactive WebClient instances.
 * 
 * This configuration demonstrates reactive HTTP client setup with:
 * - Non-blocking HTTP operations
 * - Reactive error handling
 * - Timeout management
 * - Memory buffer configuration
 * 
 * Key reactive features:
 * 1. Non-blocking HTTP client
 * 2. Reactive timeout handling
 * 3. Reactive error propagation
 * 4. Backpressure support
 */
@Configuration
public class WebClientConfig {

    /**
     * Creates a default WebClient with reactive configurations.
     * 
     * Reactive features:
     * - Non-blocking HTTP operations
     * - Reactive timeout handling with onErrorResume
     * - Memory buffer configuration for large responses
     * - Distributed tracing support
     * 
     * @param builder WebClient builder for configuration
     * @param tracer Distributed tracing support
     * @return Configured WebClient instance
     */
    @Bean
    public WebClient webClient(WebClient.Builder builder, Tracer tracer) {
        return builder
                // Configure maximum memory buffer size for responses
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024))
                // Add reactive timeout and error handling
                .filter((request, next) -> next.exchange(request)
                        .timeout(Duration.ofSeconds(10))
                        .onErrorResume(e -> {
                            if (e instanceof TimeoutException) {
                                return Mono.error(new RuntimeException("API request timed out", e));
                            }
                            return Mono.error(e);
                        }))
                .build();
    }

    /**
     * Creates a specialized WebClient for review service communication.
     * 
     * Reactive features:
     * - Non-blocking HTTP operations
     * - Base URL configuration
     * - Dedicated client for review service
     * 
     * @return Configured WebClient instance for review service
     */
    @Bean
    @Qualifier("reviewWebClient")
    public WebClient reviewWebClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8081")
                .build();
    }
}
