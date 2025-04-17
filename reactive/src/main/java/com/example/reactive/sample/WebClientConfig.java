package com.example.reactive.sample;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient(WebClient.Builder builder, Tracer tracer) {
        return builder
            .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024))
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
}
}
