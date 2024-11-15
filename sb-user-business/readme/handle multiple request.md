In Spring Boot, multiple requests are typically handled in **parallel** by default, thanks to the underlying use of **Servlets** and **thread pools** in the Spring framework. Spring Boot applications are inherently **multithreaded** because they run on top of a web server like **Tomcat**, **Jetty**, or **Undertow**, which supports concurrent request processing.

Here’s a breakdown of how Spring Boot handles concurrent requests:

### **1. Threading Model in Spring Boot (Tomcat Example)**

When a Spring Boot application receives multiple requests at the same time, it delegates the request handling to the **Servlet container** (e.g., **Tomcat**, the default embedded web server in Spring Boot). Tomcat (or another container) uses a **thread pool** to handle requests concurrently.

- **Each incoming request is processed by a separate thread** from the thread pool.
- **Multiple requests are processed in parallel** (assuming enough threads are available in the pool).
- The thread pool allows the application to serve many users simultaneously without blocking other requests.

### **2. Default Behavior: Parallel Processing with a Thread Pool**

When using the default embedded **Tomcat** server, Spring Boot creates a thread pool with a default configuration. Here's how it works:
- When a request arrives, a thread is pulled from the pool to handle the request.
- If more requests arrive simultaneously, more threads from the pool are used to process them concurrently.
- If all threads are busy, requests may be queued until a thread becomes available.

The default thread pool in Tomcat has a certain size, and its configuration can be customized.

### **3. Customizing the Thread Pool Configuration**

You can adjust the size of the thread pool by configuring properties in the `application.properties` or `application.yml` file. This can be useful if you expect a high volume of concurrent requests.

For example, to configure the thread pool for **Tomcat**:

```properties
server.tomcat.threads.max=200  # Maximum number of threads in the pool
server.tomcat.threads.min-spare=10  # Minimum idle threads in the pool
```

This configuration sets the maximum number of threads to 200 and ensures that at least 10 threads are kept idle, ready to handle incoming requests.

### **4. Asynchronous Request Handling**

While Spring Boot can handle requests in parallel, it is still synchronous by default (i.e., each request will block a thread until processing is complete). If you want **non-blocking** and **asynchronous** request processing, Spring Boot and Spring WebFlux (reactive programming) provide support for **asynchronous** and **non-blocking** requests.

Here’s how you can use asynchronous request handling with traditional Spring MVC:

#### **Using `@Async` for Asynchronous Processing in Spring MVC**:

You can make methods in your controller asynchronous using the `@Async` annotation. This allows long-running tasks to be executed in a separate thread while freeing up the main thread for handling other requests.

1. **Enable Asynchronous Processing**:
   Add `@EnableAsync` to your configuration class or main application class to enable asynchronous method execution.

   ```java
   @SpringBootApplication
   @EnableAsync
   public class Application {
       public static void main(String[] args) {
           SpringApplication.run(Application.class, args);
       }
   }
   ```

2. **Use `@Async` in a Service Method**:
   ```java
   import org.springframework.scheduling.annotation.Async;
   import org.springframework.stereotype.Service;

   @Service
   public class MyService {

       @Async
       public void processAsync() {
           // Long-running task
           System.out.println("Processing in separate thread");
       }
   }
   ```

3. **Controller**:
   When calling this asynchronous method from your controller, the request thread will return immediately, and the actual processing will happen asynchronously.

   ```java
   @RestController
   public class MyController {

       @Autowired
       private MyService myService;

       @GetMapping("/process")
       public String processRequest() {
           myService.processAsync();
           return "Request is being processed asynchronously";
       }
   }
   ```

With this setup, the method `processAsync()` will be executed in a separate thread, and the response will be sent back to the client immediately.

#### **Using Spring WebFlux for Fully Reactive Programming**:

Spring WebFlux is a more advanced and fully reactive approach for handling high volumes of concurrent requests using a non-blocking, reactive programming model. Instead of using traditional threads, WebFlux relies on an event loop and async I/O, allowing it to handle many more requests concurrently without being limited by the number of threads.

This approach is ideal for building highly scalable, non-blocking applications, but it's not the default in Spring Boot, which typically uses Spring MVC and blocking I/O.

### **5. Thread Safety in Spring Boot**

Since multiple requests are handled in parallel, it's important to ensure **thread safety** within your Spring Beans, especially those with **stateful** behavior. By default, Spring Beans are **singleton**, meaning only one instance of a bean is shared across all requests.

To ensure thread safety:
- **Stateless Beans**: Beans should be stateless, meaning they don't hold request-specific data. This is typically the default for services.
- **Scope**: If you need a stateful bean, use **request** or **session** scope (`@Scope("request")` or `@Scope("session")`).
- **Thread-safe Collections**: If you use collections or mutable fields, ensure they are thread-safe (e.g., `ConcurrentHashMap`, `synchronized` blocks, etc.).

### **Summary**:
- **Spring Boot handles requests in parallel by default**, using a thread pool provided by the embedded web server (Tomcat, Jetty, etc.).
- Each request is processed by a separate thread from the thread pool.
- The thread pool configuration can be customized (e.g., maximum thread size).
- For non-blocking, asynchronous processing, you can use `@Async` in Spring MVC or use the fully reactive **Spring WebFlux** for scalable, non-blocking applications.
- Ensure **thread safety** in your beans when handling concurrent requests.

By default, Spring Boot handles multiple requests concurrently and in parallel, efficiently managing each request with threads.