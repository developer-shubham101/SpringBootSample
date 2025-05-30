Okay, let's embark on a step-by-step journey to learn Reactive Programming with Spring Boot (using Project Reactor and Spring WebFlux).

This document will guide you from the absolute basics to building a simple reactive application.

**Reactive Programming with Spring Boot: A Step-by-Step Guide**

**Goal:** Understand reactive principles and build a simple reactive REST API with Spring WebFlux, including reactive data access.

**Prerequisites:**
1.  **Java 8+:** Lambdas and functional interfaces are heavily used.
2.  **Spring Boot Basics:** Familiarity with Spring Boot concepts (beans, dependency injection, starters).
3.  **Maven or Gradle:** For project setup and dependency management.
4.  **An IDE:** IntelliJ IDEA (Community or Ultimate) or Eclipse/STS.

---

**Step 0: What is Reactive Programming & Why?**

*   **What:** An asynchronous programming paradigm concerned with data streams and the propagation of change. It's about reacting to events/data as they happen.
*   **Key Principles:**
    *   **Responsive:** Respond in a timely manner.
    *   **Resilient:** Stay responsive in the face of failure.
    *   **Elastic:** Stay responsive under varying workload.
    *   **Message Driven:** Relies on asynchronous message-passing.
*   **Why in Web Applications?**
    *   **Scalability:** Handles more concurrent users with fewer threads, leading to better resource utilization.
    *   **Non-Blocking I/O:** Operations (like network calls, database queries) don't block the calling thread, freeing it up to handle other requests.
    *   **Backpressure:** Allows consumers to signal producers how much data they can handle, preventing overload.

**Spring's Reactive Stack:**
*   **Project Reactor:** The reactive library Spring uses (provides `Mono` and `Flux`).
*   **Spring WebFlux:** Reactive web framework, an alternative to Spring MVC.
*   **Reactive Data Access:** Support for reactive drivers (R2DBC for SQL, reactive MongoDB, Cassandra, Redis drivers).
*   **`WebClient`:** Non-blocking, reactive HTTP client.

---

**Step 1: Setting up a Reactive Spring Boot Project**

1.  Go to [https://start.spring.io/](https://start.spring.io/)
2.  Choose:
    *   **Project:** Maven or Gradle
    *   **Language:** Java
    *   **Spring Boot:** Latest stable version
    *   **Project Metadata:**
        *   Group: `com.example`
        *   Artifact: `reactive-learning`
        *   Name: `reactive-learning`
        *   Description: Demo project for Spring Boot Reactive
        *   Package name: `com.example.reactivelearning`
    *   **Packaging:** Jar
    *   **Java:** 17 (or 11, or 8 if you must)
3.  **Dependencies:**
    *   `Spring Reactive Web` (this brings in WebFlux and Reactor)
    *   `Lombok` (optional, for boilerplate code reduction)
    *   `Spring Data R2DBC` (for reactive SQL database access)
    *   `H2 Database` (for an in-memory reactive SQL database)
4.  Click **GENERATE**.
5.  Download the ZIP, extract it, and import it into your IDE.

---

**Step 2: Understanding Core Reactive Types: `Mono` and `Flux` (Project Reactor)**

`Mono` and `Flux` are *Publishers*. They emit data.
*   **`Mono<T>`:** Emits 0 or 1 item. Useful for operations that return a single result or nothing (e.g., findById, save an entity).
*   **`Flux<T>`:** Emits 0 to N items. Useful for operations that return multiple results (e.g., findAll, streaming data).

**Key Idea: Nothing Happens Until You Subscribe!**
Reactive streams are lazy. The publisher won't start emitting data until someone subscribes to it.

Let's create a simple class to play with them.

`src/main/java/com/example/reactivelearning/ReactorBasics.java`:
```java
package com.example.reactivelearning;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class ReactorBasics {

    public static void main(String[] args) {
        System.out.println("--- Mono Examples ---");
        monoExample();
        monoErrorExample();

        System.out.println("\n--- Flux Examples ---");
        fluxExample();
        fluxWithMapAndFilter();
        fluxFromIterable();
        fluxWithErrorHandling();
        fluxWithDelayAndLogging(); // This will run asynchronously

        // Keep the main thread alive for a bit to see async operations
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Main thread finished.");
    }

    public static void monoExample() {
        System.out.println("monoExample:");
        Mono<String> mono = Mono.just("Hello, Reactor!");
        mono.subscribe(
            data -> System.out.println("Data: " + data),      // onNext
            error -> System.err.println("Error: " + error),  // onError
            () -> System.out.println("Completed Mono")       // onComplete
        );

        Mono<String> emptyMono = Mono.empty();
        emptyMono.subscribe(
            data -> System.out.println("Data from empty: " + data),
            error -> System.err.println("Error from empty: " + error),
            () -> System.out.println("Completed Empty Mono (no data emitted)")
        );
    }

    public static void monoErrorExample() {
        System.out.println("monoErrorExample:");
        Mono<String> errorMono = Mono.error(new RuntimeException("Oops, Mono error!"));
        errorMono.subscribe(
            data -> System.out.println("Data: " + data),
            error -> System.err.println("Error caught: " + error.getMessage()),
            () -> System.out.println("Completed (won't be called if error)")
        );
    }

    public static void fluxExample() {
        System.out.println("fluxExample:");
        Flux<Integer> flux = Flux.just(1, 2, 3, 4, 5);
        flux.subscribe(
            data -> System.out.println("Flux Data: " + data),
            error -> System.err.println("Flux Error: " + error),
            () -> System.out.println("Completed Flux")
        );
    }

    public static void fluxFromIterable() {
        System.out.println("fluxFromIterable:");
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
        Flux<String> namesFlux = Flux.fromIterable(names);
        namesFlux.subscribe(
            name -> System.out.println("Name: " + name),
            Throwable::printStackTrace, // Method reference for error handling
            () -> System.out.println("Names Flux completed")
        );
    }

    public static void fluxWithMapAndFilter() {
        System.out.println("fluxWithMapAndFilter:");
        Flux.just(1, 2, 3, 4, 5, 6)
            .filter(num -> num % 2 == 0)         // Keep only even numbers
            .map(num -> "Number: " + (num * 10)) // Transform them
            .subscribe(System.out::println);     // Print each transformed item
    }

    public static void fluxWithErrorHandling() {
        System.out.println("fluxWithErrorHandling:");
        Flux.just(1, 2, 0, 4, 5) // Division by zero will cause an error
            .map(num -> 100 / num)
            .onErrorReturn(-1) // If an error occurs, return -1 and complete
            // .onErrorResume(e -> Mono.just(-2)) // Alternative: resume with another Mono/Flux
            // .doOnError(e -> System.err.println("Error happened: " + e.getMessage())) // Side-effect on error
            .subscribe(
                data -> System.out.println("Safe Division: " + data),
                error -> System.err.println("This shouldn't be printed if onErrorReturn is used: " + error),
                () -> System.out.println("Safe Division Flux completed")
            );
    }

    public static void fluxWithDelayAndLogging() {
        System.out.println("fluxWithDelayAndLogging (async):");
        Flux.range(1, 3)
            .delayElements(Duration.ofSeconds(1)) // Emit elements with a delay
            .log() // Logs all reactive signals (onSubscribe, request, onNext, etc.)
            .map(i -> "Delayed item " + i)
            .subscribe(
                System.out::println,
                Throwable::printStackTrace,
                () -> System.out.println("Delayed Flux completed")
            );
    }
}
```
Run this `ReactorBasics` class. Observe the output carefully, especially the order of operations and how errors are handled. The `.log()` operator is very useful for debugging reactive streams.

---

**Step 3: Creating a Simple Reactive Controller (Spring WebFlux)**

WebFlux supports two programming models:
1.  **Annotation-based:** Similar to Spring MVC (`@RestController`, `@GetMapping`, etc.).
2.  **Functional:** Using HandlerFunctions and RouterFunctions (more programmatic).

We'll start with annotation-based.

Create a `Greeting` class:
`src/main/java/com/example/reactivelearning/Greeting.java`:
```java
package com.example.reactivelearning;

// Using Lombok for getters, setters, constructor (optional)
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Greeting {
    private String message;
}
```

Create a `GreetingController`:
`src/main/java/com/example/reactivelearning/GreetingController.java`:
```java
package com.example.reactivelearning;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
public class GreetingController {

    @GetMapping("/hello")
    public Mono<String> hello() {
        return Mono.just("Hello, Reactive World!");
    }

    @GetMapping("/greeting/{name}")
    public Mono<Greeting> greeting(@PathVariable String name) {
        return Mono.just(new Greeting("Hello, " + name + "!"));
    }

    // Server-Sent Events (SSE) endpoint
    @GetMapping(value = "/greetings/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Greeting> streamGreetings() {
        return Flux.interval(Duration.ofSeconds(1)) // Emit every second
                   .map(sequence -> new Greeting("Stream Greeting #" + sequence))
                   .take(10); // Emit 10 greetings then complete
    }
}
```
Now, run your main Spring Boot application (`ReactiveLearningApplication.java`).
*   Open your browser or Postman:
    *   `http://localhost:8080/hello` -> You'll see "Hello, Reactive World!"
    *   `http://localhost:8080/greeting/YourName` -> You'll see `{"message":"Hello, YourName!"}`
    *   `http://localhost:8080/greetings/stream` -> You'll see data streaming in every second (best viewed in a browser that supports SSE well or curl).

**Explanation:**
*   Methods return `Mono<T>` or `Flux<T>`.
*   Spring WebFlux handles subscribing to these publishers and writing the data to the HTTP response.
*   `MediaType.TEXT_EVENT_STREAM_VALUE` is used for Server-Sent Events, where the server continuously pushes data to the client.

---

**Step 4: Reactive Data Access with R2DBC and H2**

R2DBC (Reactive Relational Database Connectivity) provides reactive APIs for SQL databases.

1.  **Configure H2 Database (Reactive)**
    `src/main/resources/application.properties`:
    ```properties
    # H2 R2DBC Configuration
    spring.r2dbc.url=r2dbc:h2:mem:///testdb;DB_CLOSE_DELAY=-1;
    spring.r2dbc.username=sa
    spring.r2dbc.password=
    # For H2 console (optional, useful for debugging, but it's a servlet so runs on a separate thread pool)
    # spring.h2.console.enabled=true
    # spring.h2.console.path=/h2-console

    # Initialize schema - IMPORTANT for R2DBC
    spring.sql.init.mode=always
    # spring.sql.init.schema-locations=classpath:schema.sql # If you have schema.sql
    ```
    *Note: `DB_CLOSE_DELAY=-1` keeps the H2 in-memory database alive as long as the JVM is running.*

2.  **Create a SQL schema file (optional, but good practice)**
    `src/main/resources/schema.sql`:
    ```sql
    CREATE TABLE IF NOT EXISTS item (
        id BIGINT AUTO_INCREMENT PRIMARY KEY,
        name VARCHAR(255) NOT NULL,
        price DOUBLE NOT NULL
    );

    -- Insert some initial data (optional)
    -- INSERT INTO item (name, price) VALUES ('Laptop', 1200.50);
    -- INSERT INTO item (name, price) VALUES ('Mouse', 25.00);
    ```
    Make sure `spring.sql.init.schema-locations=classpath:schema.sql` is uncommented in `application.properties` if you use this.
    Alternatively, you can let Spring Data R2DBC try to create the schema from your entity, but explicitly defining it is often more robust.

3.  **Create an Entity**
    `src/main/java/com/example/reactivelearning/Item.java`:
    ```java
    package com.example.reactivelearning;

    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;
    import org.springframework.data.annotation.Id;
    import org.springframework.data.relational.core.mapping.Table;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Table("item") // Maps this class to the "item" table
    public class Item {
        @Id // Marks this field as the primary key
        private Long id;
        private String name;
        private double price;

        // Constructor without id for creating new items
        public Item(String name, double price) {
            this.name = name;
            this.price = price;
        }
    }
    ```

4.  **Create a Reactive Repository**
    `src/main/java/com/example/reactivelearning/ItemRepository.java`:
    ```java
    package com.example.reactivelearning;

    import org.springframework.data.repository.reactive.ReactiveCrudRepository;
    import org.springframework.stereotype.Repository;
    import reactor.core.publisher.Flux;

    @Repository
    public interface ItemRepository extends ReactiveCrudRepository<Item, Long> {
        Flux<Item> findByNameContaining(String name); // Custom query method
    }
    ```
    `ReactiveCrudRepository` provides reactive versions of common CRUD operations (e.g., `save`, `findById`, `findAll`, `deleteById`). They return `Mono` or `Flux`.

5.  **Create a Service (Optional but good practice)**
    `src/main/java/com/example/reactivelearning/ItemService.java`:
    ```java
    package com.example.reactivelearning;

    import org.springframework.stereotype.Service;
    import reactor.core.publisher.Flux;
    import reactor.core.publisher.Mono;

    @Service
    public class ItemService {

        private final ItemRepository itemRepository;

        public ItemService(ItemRepository itemRepository) {
            this.itemRepository = itemRepository;
        }

        public Flux<Item> getAllItems() {
            return itemRepository.findAll();
        }

        public Mono<Item> getItemById(Long id) {
            return itemRepository.findById(id);
        }

        public Mono<Item> createItem(Item item) {
            return itemRepository.save(item);
        }

        public Mono<Item> updateItem(Long id, Item item) {
            return itemRepository.findById(id)
                .flatMap(existingItem -> {
                    existingItem.setName(item.getName());
                    existingItem.setPrice(item.getPrice());
                    return itemRepository.save(existingItem);
                });
        }

        public Mono<Void> deleteItem(Long id) {
            return itemRepository.deleteById(id);
        }

        public Flux<Item> findItemsByName(String name) {
            return itemRepository.findByNameContaining(name);
        }
    }
    ```
    *   `flatMap` is crucial here. If `findById` returns an item (`Mono<Item>`), `flatMap` allows you to perform another reactive operation (like `save`) using that item and return its result (`Mono<Item>`). If you used `map`, you'd get `Mono<Mono<Item>>`, which is usually not what you want.

6.  **Create a Controller for Items**
    `src/main/java/com/example/reactivelearning/ItemController.java`:
    ```java
    package com.example.reactivelearning;

    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;
    import reactor.core.publisher.Flux;
    import reactor.core.publisher.Mono;

    @RestController
    @RequestMapping("/api/items")
    public class ItemController {

        private final ItemService itemService;

        public ItemController(ItemService itemService) {
            this.itemService = itemService;
        }

        @GetMapping
        public Flux<Item> getAllItems() {
            return itemService.getAllItems();
        }

        @GetMapping("/{id}")
        public Mono<ResponseEntity<Item>> getItemById(@PathVariable Long id) {
            return itemService.getItemById(id)
                    .map(ResponseEntity::ok) // If item found, wrap in ResponseEntity.ok()
                    .defaultIfEmpty(ResponseEntity.notFound().build()); // If Mono is empty, return 404
        }

        @PostMapping
        @ResponseStatus(HttpStatus.CREATED)
        public Mono<Item> createItem(@RequestBody Item item) {
            return itemService.createItem(item);
        }

        @PutMapping("/{id}")
        public Mono<ResponseEntity<Item>> updateItem(@PathVariable Long id, @RequestBody Item item) {
            return itemService.updateItem(id, item)
                    .map(ResponseEntity::ok)
                    .defaultIfEmpty(ResponseEntity.notFound().build());
        }

        @DeleteMapping("/{id}")
        public Mono<ResponseEntity<Void>> deleteItem(@PathVariable Long id) {
            return itemService.deleteItem(id)
                    .then(Mono.just(ResponseEntity.noContent().<Void>build())) // After delete, return 204
                    .defaultIfEmpty(ResponseEntity.notFound().build()); // Should not happen with deleteById typically, but good for safety
        }

        @GetMapping("/search")
        public Flux<Item> searchItemsByName(@RequestParam String name) {
            return itemService.findItemsByName(name);
        }
    }
    ```

7.  **Data Initializer (Optional, for populating DB on startup)**
    `src/main/java/com/example/reactivelearning/DataInitializer.java`:
    ```java
    package com.example.reactivelearning;

    import lombok.extern.slf4j.Slf4j;
    import org.springframework.boot.CommandLineRunner;
    import org.springframework.stereotype.Component;
    import reactor.core.publisher.Flux;

    import java.util.Arrays;

    @Component
    @Slf4j
    public class DataInitializer implements CommandLineRunner {

        private final ItemRepository itemRepository;

        public DataInitializer(ItemRepository itemRepository) {
            this.itemRepository = itemRepository;
        }

        @Override
        public void run(String... args) throws Exception {
            log.info("Starting data initialization...");
            itemRepository.deleteAll() // Clear existing data
                .thenMany(
                    Flux.fromIterable(Arrays.asList(
                        new Item("Laptop X2000", 1299.99),
                        new Item("Wireless Mouse", 24.50),
                        new Item("Mechanical Keyboard", 89.95),
                        new Item("4K Monitor", 349.00)
                    ))
                    .flatMap(itemRepository::save) // Save each item
                )
                .doOnNext(item -> log.info("Saved item: {}", item.getName()))
                .doOnComplete(() -> log.info("Data initialization complete."))
                .subscribe(
                    null, // onNext is handled by doOnNext
                    error -> log.error("Error during data initialization: ", error)
                );
        }
    }
    ```
    *   `CommandLineRunner` ensures this code runs after the application context is loaded.
    *   We use reactive operators (`deleteAll`, `thenMany`, `flatMap`, `doOnNext`, `doOnComplete`) to perform operations sequentially.
    *   **Crucially, we `.subscribe()` at the end.** Without it, the data initialization RDD (Reactive Data Definition) stream would not execute.

Restart your application. You should see logs from `DataInitializer`.
Now you can test your Item API endpoints using Postman or curl:
*   `GET http://localhost:8080/api/items`
*   `POST http://localhost:8080/api/items` with JSON body: `{"name": "New Gadget", "price": 19.99}`
*   `GET http://localhost:8080/api/items/1` (or other ID)
*   `PUT http://localhost:8080/api/items/1` with JSON body: `{"name": "Updated Gadget", "price": 22.50}`
*   `GET http://localhost:8080/api/items/search?name=Laptop`
*   `DELETE http://localhost:8080/api/items/1`

---

**Step 5: Using `WebClient` (Reactive HTTP Client)**

`WebClient` is the reactive, non-blocking alternative to `RestTemplate`.

Let's say you want to call an external (or another internal) API.
First, add `WebClient` as a bean (optional, but good for centralized configuration).
`src/main/java/com/example/reactivelearning/config/WebClientConfig.java` (Create a `config` package)
```java
package com.example.reactivelearning.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient localApiClient(WebClient.Builder builder) {
        return builder.baseUrl("http://localhost:8080/api").build();
    }

    @Bean
    public WebClient externalApiClient(WebClient.Builder builder) {
        // Example: Public JSONPlaceholder API
        return builder.baseUrl("https://jsonplaceholder.typicode.com").build();
    }
}
```

Now, let's create a service that uses `WebClient`.
`src/main/java/com/example/reactivelearning/ExternalApiService.java`:
```java
package com.example.reactivelearning;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

// A dummy class to map JSONPlaceholder response
@lombok.Data @lombok.NoArgsConstructor @lombok.AllArgsConstructor
class Post {
    private int userId;
    private int id;
    private String title;
    private String body;
}


@Service
public class ExternalApiService {

    private final WebClient localApiClient;
    private final WebClient externalApiClient;

    public ExternalApiService(@Qualifier("localApiClient") WebClient localApiClient,
                              @Qualifier("externalApiClient") WebClient externalApiClient) {
        this.localApiClient = localApiClient;
        this.externalApiClient = externalApiClient;
    }

    public Flux<Item> fetchAllLocalItems() {
        return localApiClient.get()
                .uri("/items")
                .retrieve() // Gets the response body
                .bodyToFlux(Item.class) // Converts body to Flux<Item>
                .doOnError(error -> System.err.println("Error fetching local items: " + error.getMessage()));
    }

    public Mono<Item> fetchLocalItemById(Long id) {
        return localApiClient.get()
                .uri("/items/{id}", id) // URI templating
                .retrieve()
                .bodyToMono(Item.class)
                .onErrorResume(e -> { // Example: return an empty Mono if not found or error
                    System.err.println("Error fetching item " + id + ": " + e.getMessage());
                    return Mono.empty();
                });
    }

    public Flux<Post> fetchExternalPosts() {
        return externalApiClient.get()
                .uri("/posts")
                .retrieve()
                .bodyToFlux(Post.class)
                .take(5); // Take only the first 5 posts for brevity
    }

    public Mono<Post> fetchExternalPostById(int id) {
        return externalApiClient.get()
                .uri("/posts/{id}", id)
                .retrieve()
                .bodyToMono(Post.class);
    }
}
```
And a controller to expose this:
`src/main/java/com/example/reactivelearning/ExternalApiController.java`:
```java
package com.example.reactivelearning;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/external")
public class ExternalApiController {

    private final ExternalApiService externalApiService;

    public ExternalApiController(ExternalApiService externalApiService) {
        this.externalApiService = externalApiService;
    }

    @GetMapping("/local/items")
    public Flux<Item> getLocalItemsViaWebClient() {
        return externalApiService.fetchAllLocalItems();
    }

    @GetMapping("/local/items/{id}")
    public Mono<Item> getLocalItemByIdViaWebClient(@PathVariable Long id) {
        return externalApiService.fetchLocalItemById(id);
    }

    @GetMapping("/posts")
    public Flux<Post> getExternalPosts() {
        return externalApiService.fetchExternalPosts();
    }

    @GetMapping("/posts/{id}")
    public Mono<Post> getExternalPostById(@PathVariable int id) {
        return externalApiService.fetchExternalPostById(id);
    }
}
```
Restart and test:
*   `GET http://localhost:8080/external/local/items` (calls your own `/api/items` endpoint)
*   `GET http://localhost:8080/external/posts` (calls `jsonplaceholder.typicode.com/posts`)

---

**Step 6: Testing Reactive Code**

Spring Boot provides `reactor-test` for testing `Mono` and `Flux` publishers.
Add `reactor-test` if not already present (usually included with `spring-boot-starter-webflux`):
```xml
<!-- For Maven -->
<dependency>
    <groupId>io.projectreactor</groupId>
    <artifactId>reactor-test</artifactId>
    <scope>test</scope>
</dependency>
```

Example: Test `ItemService`.
`src/test/java/com/example/reactivelearning/ItemServiceTest.java`:
```java
package com.example.reactivelearning;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks // Injects mocked itemRepository into itemService
    private ItemService itemService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initializes mocks
    }

    @Test
    void getAllItems_shouldReturnFluxOfItems() {
        Item item1 = new Item(1L, "Item 1", 10.0);
        Item item2 = new Item(2L, "Item 2", 20.0);
        when(itemRepository.findAll()).thenReturn(Flux.just(item1, item2));

        Flux<Item> items = itemService.getAllItems();

        StepVerifier.create(items)
            .expectNext(item1)
            .expectNext(item2)
            .verifyComplete(); // Verifies that the Flux completes after emitting expected items
    }

    @Test
    void getItemById_whenItemExists_shouldReturnMonoOfItem() {
        Item item = new Item(1L, "Test Item", 100.0);
        when(itemRepository.findById(1L)).thenReturn(Mono.just(item));

        Mono<Item> result = itemService.getItemById(1L);

        StepVerifier.create(result)
            .expectNextMatches(retrievedItem ->
                retrievedItem.getId().equals(1L) &&
                retrievedItem.getName().equals("Test Item")
            )
            .verifyComplete();
    }

    @Test
    void getItemById_whenItemDoesNotExist_shouldReturnEmptyMono() {
        when(itemRepository.findById(anyLong())).thenReturn(Mono.empty());

        Mono<Item> result = itemService.getItemById(99L);

        StepVerifier.create(result)
            .expectNextCount(0) // Expect no items
            .verifyComplete();
    }

    @Test
    void createItem_shouldSaveAndReturnItem() {
        Item newItem = new Item("New Item", 50.0);
        Item savedItem = new Item(1L, "New Item", 50.0); // Item with ID after save
        when(itemRepository.save(any(Item.class))).thenReturn(Mono.just(savedItem));

        Mono<Item> result = itemService.createItem(newItem);

        StepVerifier.create(result)
            .expectNext(savedItem)
            .verifyComplete();
    }
}
```
**`StepVerifier`** is the main tool:
*   `create(publisher)`: Starts verification for a `Mono` or `Flux`.
*   `expectNext(value)`: Asserts the next emitted value.
*   `expectNextCount(count)`: Asserts a number of items are emitted.
*   `expectNextMatches(predicate)`: Asserts the next item matches a condition.
*   `expectComplete()`: Asserts the stream completes successfully.
*   `expectError(class)`: Asserts the stream terminates with an error of a specific type.
*   `verifyComplete()`: Triggers the subscription and verifies all expectations.
*   `verify()`: A shorthand for `expectComplete().verify()`.
*   There are many other useful methods like `thenRequest`, `thenCancel`, etc.

---

**Step 7: Understanding Backpressure**

Backpressure is a mechanism by which a subscriber can control the rate at which a publisher emits items. This prevents the subscriber from being overwhelmed.

*   Project Reactor handles backpressure automatically in many cases (e.g., when bridging to non-reactive systems or when a subscriber explicitly requests a certain number of items).
*   When you `subscribe()` without arguments, or with just `onNext`, `onError`, `onComplete` lambdas, an unbounded request (`Long.MAX_VALUE`) is made by default.
*   You can implement custom `Subscriber`s to control demand or use operators like:
    *   `limitRate(n)`: Requests `n` items at a time from upstream.
    *   `buffer(n)`: Collects items into lists of size `n`.
    *   `onBackpressureDrop()`, `onBackpressureBuffer()`, `onBackpressureLatest()`: Strategies for when the downstream can't keep up.

Let's see a simple `limitRate` example in `ReactorBasics.java`:
```java
// Add this method to ReactorBasics.java
public static void fluxWithBackpressure() {
    System.out.println("fluxWithBackpressure (limitRate):");
    Flux.range(1, 20) // Emits 1 to 20
        .log() // Log signals to see requests
        .limitRate(5) // Request 5 items, then 75% of 5 (i.e., 3 or 4) on subsequent requests
        .subscribe(
            data -> {
                System.out.println("Processing: " + data);
                // Simulate work
                try { Thread.sleep(100); } catch (InterruptedException e) {}
            },
            error -> System.err.println("Error: " + error),
            () -> System.out.println("Backpressure Flux completed")
        );
}

// Call it from main:
// public static void main(String[] args) {
// ...
// fluxWithBackpressure();
// ...
// Thread.sleep(5000); // might need to increase this
// }
```
Observe the `request(n)` logs. You'll see that the subscriber initially requests 5, and then after processing some, it requests more in smaller batches controlled by `limitRate`.

---

**Step 8: Error Handling in Reactive Streams**

We've seen `onErrorReturn` and `onErrorResume`. Other common operators:
*   `onErrorMap(Function<Throwable, Throwable> mapper)`: Transform an error into another error.
*   `doOnError(Consumer<Throwable> errorConsumer)`: Perform a side-effect when an error occurs (e.g., logging) without altering the error signal.
*   `retry(long n)`: Retry the subscription `n` times if an error occurs.
*   `retryWhen(Retry retrySpec)`: More advanced retry logic based on a companion `Flux`.

Example in `ReactorBasics.java`:
```java
// Add this method to ReactorBasics.java
public static void fluxWithRetry() {
    System.out.println("fluxWithRetry:");
    final int[] attempt = {0}; // Effectively final array for lambda
    Flux.range(1, 5)
        .map(i -> {
            if (i == 3 && attempt[0] < 2) { // Fail on 3 for the first two attempts
                attempt[0]++;
                System.out.println("Simulating error at " + i + ", attempt " + attempt[0]);
                throw new RuntimeException("Simulated error");
            }
            return "Item: " + i;
        })
        .retry(2) // Retry up to 2 times on error
        .subscribe(
            System.out::println,
            error -> System.err.println("Final Error (after retries): " + error.getMessage()),
            () -> System.out.println("Retry Flux completed")
        );
}
// Call it from main:
// public static void main(String[] args) {
// ...
// fluxWithRetry();
// ...
// }
```

---

**Further Learning & Advanced Topics:**

1.  **Schedulers:** Controlling on which threads reactive operations execute (`Schedulers.parallel()`, `Schedulers.boundedElastic()` for blocking I/O, `Schedulers.single()`, `publishOn()`, `subscribeOn()`).
2.  **Functional Web Endpoints:** `RouterFunction` and `HandlerFunction` as an alternative to annotated controllers.
3.  **Reactive Security:** `spring-boot-starter-security` works with WebFlux.
4.  **Hot vs. Cold Publishers:**
    *   **Cold:** Start emitting data from the beginning for each new subscriber (e.g., `Flux.just()`, HTTP call `Mono`). Most publishers are cold.
    *   **Hot:** Emit data regardless of subscribers. New subscribers might miss earlier emissions (e.g., UI events, stock tickers). Operators like `share()`, `publish().autoConnect()` can make cold publishers hot.
5.  **Context Propagation:** `Mono.contextWrite()` and `Mono.deferContextual()` for passing contextual information (like tracing IDs) through reactive streams.
6.  **Blocking Operations:** How to handle them safely in a reactive environment (e.g., using `Mono.fromCallable(...).subscribeOn(Schedulers.boundedElastic())`).
7.  **RSocket:** A reactive, binary protocol for client-server communication.
8.  **BlockHound:** A library to detect accidental blocking calls in reactive code.

---

**Conclusion:**

You've now covered the fundamentals of reactive programming with Spring Boot:
*   Understood `Mono` and `Flux`.
*   Built reactive REST APIs using WebFlux.
*   Performed reactive data access with R2DBC.
*   Used `WebClient` for reactive HTTP calls.
*   Written tests for reactive components.
*   Touched on backpressure and error handling.

Reactive programming is a paradigm shift. The key is to "think reactively" – everything is a stream of events. Avoid blocking, compose operations using operators, and remember: **nothing happens until you subscribe!**

Keep practicing, explore the operators in Project Reactor's documentation, and build more complex applications to solidify your understanding. Good luck!