package com.example.reactive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

/**
 * Main application class for the Reactive Spring Boot application.
 * 
 * This application demonstrates reactive programming concepts using:
 * - Spring WebFlux for reactive web endpoints
 * - Project Reactor (Mono/Flux) for reactive streams
 * - Reactive MongoDB for non-blocking database operations
 * 
 * Key reactive features:
 * 1. Non-blocking I/O operations
 * 2. Backpressure handling
 * 3. Reactive streams throughout the application
 * 4. Event-driven architecture
 */
@SpringBootApplication
@EnableReactiveMongoRepositories // Enables reactive MongoDB repositories for non-blocking database operations
public class ReactiveApplication {

	/**
	 * Main method that bootstraps the reactive Spring Boot application.
	 * 
	 * The application starts with a reactive web server (Netty by default)
	 * and configures all components for non-blocking operations.
	 * 
	 * @param args Command line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(ReactiveApplication.class, args);
	}

}
