package com.example.reactive;

import com.example.reactive.service.SampleDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(SampleDataService sampleDataService) {
        return args -> {
            log.info("Initializing sample data...");
            sampleDataService.loadSampleData()
                .doOnSuccess(v -> log.info("Sample data loaded successfully"))
                .doOnError(e -> log.error("Error loading sample data", e))
                .subscribe();
        };
    }
} 