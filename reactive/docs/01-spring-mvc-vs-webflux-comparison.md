The core difference between traditional Spring MVC (servlet-based) and Spring WebFlux (reactive). Let's break it down:

**Traditional Spring MVC (Servlet-Based - e.g., Tomcat, Jetty, Undertow by default)**

*   **Thread-Per-Request Model (Simplified View):**
    *   When a new HTTP request arrives at the servlet container (like Tomcat), the container typically picks a thread from a **thread pool** to handle that request.
    *   This single thread is responsible for executing all the code for that request: controller logic, service calls, database interactions, etc.
    *   **Crucially, if any part of this processing involves blocking I/O (Input/Output) operations** – like waiting for a database query to return, calling an external HTTP service, or reading from a file – **that thread is blocked**. It sits idle, consuming resources (memory for its stack), but not doing any useful CPU work until the I/O operation completes.
    *   To handle many concurrent requests, you need a large thread pool. If all threads in the pool are busy (mostly blocked waiting for I/O), new incoming requests have to wait in a queue, leading to increased latency and potential request timeouts.
    *   **Scalability Limitation:** This model scales by adding more threads. However, threads are not free; each has a memory footprint and context-switching overhead. Beyond a certain point, adding more threads leads to diminishing returns or even performance degradation.

**Spring WebFlux (Reactive - e.g., Netty by default)**

*   **Event Loop Model / Fewer Threads:**
    *   WebFlux, by default, uses an **event loop architecture**, often powered by a server like Netty.
    *   Netty uses a small, fixed number of threads (often equal to the number of CPU cores), called **event loop threads** or **I/O threads**.
    *   When a new HTTP request arrives:
        1.  An event loop thread accepts the connection and reads the request data.
        2.  It then **delegates the processing of the request to your reactive pipeline** (your controller methods returning `Mono` or `Flux`).
        3.  **Crucially, operations within this reactive pipeline are designed to be non-blocking.**
            *   When an I/O operation is needed (e.g., a reactive database call with R2DBC, or an HTTP call with `WebClient`):
                *   The operation is initiated.
                *   The event loop thread **does not wait**. It registers a callback for when the I/O operation completes and then **immediately becomes available to handle other incoming requests or process other ready events.**
            *   When the I/O operation finishes (e.g., data arrives from the database or the external service response is received), a notification (event) is triggered.
            *   An event loop thread (not necessarily the same one that initiated the request) picks up this event and continues processing the reactive pipeline from where it left off (executing the `onNext`, `flatMap`, `map`, etc., operators).

*   **How It Handles Multiple Calls:**
    *   Because the event loop threads are not blocked by I/O, a small number of these threads can efficiently handle a very large number of concurrent requests.
    *   Imagine a waiter in a restaurant (the event loop thread):
        *   **Traditional Waiter (Thread-per-request):** Takes one order, goes to the kitchen, *waits* for the food, brings it back. Can only serve one table at a time effectively.
        *   **Reactive Waiter (Event Loop):** Takes an order from Table 1, gives it to the kitchen. Takes an order from Table 2, gives it to the kitchen. Sees food ready for Table 1, delivers it. Sees food ready for Table 2, delivers it. The waiter is constantly moving and handling multiple tasks without getting stuck waiting for one specific thing.
    *   The "work" (your application logic defined in operators) is broken down into smaller, non-blocking chunks. These chunks are scheduled for execution on the event loop threads when their preceding operations complete.

**Key Differences in Threading for Multiple Calls:**

| Feature         | Traditional Spring MVC (Servlet)                     | Spring WebFlux (Reactive)                                 |
| :-------------- | :--------------------------------------------------- | :-------------------------------------------------------- |
| **Thread Model** | Thread-per-request (from a pool)                     | Event loop with a few threads (e.g., CPU cores)           |
| **I/O Handling**| Blocking I/O (thread waits)                          | Non-blocking I/O (thread registers callback, moves on)    |
| **Concurrency** | Many threads needed for high concurrency             | Few threads can handle high concurrency                   |
| **Resource Use**| Higher per request (thread stack, context switches)  | Lower per request (threads are mostly active)             |
| **Scalability** | Limited by thread count and resource contention      | More scalable for I/O-bound workloads                     |
| **Execution**   | Imperative, sequential within a single thread        | Declarative, asynchronous, event-driven, work scheduled on event loops |

**Important Considerations for Reactive Threading:**

1.  **Don't Block the Event Loop:** The cardinal sin in reactive programming is to perform blocking operations (like traditional JDBC calls, `Thread.sleep()`, long-running CPU-intensive tasks without yielding) directly on an event loop thread. This will stall the event loop, preventing it from handling other requests, and negate all the benefits.
2.  **Schedulers for Blocking Work:** If you *must* integrate with blocking libraries:
    *   Use `Mono.fromCallable(() -> blockingCall())` or `Flux.defer(() -> Flux.fromIterable(blockingCallThatReturnsList()))`.
    *   And, crucially, move this execution to a dedicated thread pool designed for blocking tasks using `.subscribeOn(Schedulers.boundedElastic())`. This offloads the blocking work from the event loop threads.
3.  **`publishOn` vs. `subscribeOn`:**
    *   `subscribeOn(Scheduler)`: Affects which thread the *source* publisher starts emitting on (and thus, usually, where I/O initiation happens). It influences the entire chain *upwards*.
    *   `publishOn(Scheduler)`: Changes the thread on which *downstream* operators (after `publishOn`) and the final subscriber callbacks execute. It influences the chain *downwards*.

**In summary:** Reactive Spring Boot (WebFlux) handles multiple calls by using a small number of event loop threads that are never (or rarely) blocked. They efficiently juggle numerous concurrent requests by delegating I/O operations and reacting to their completion events, allowing for much higher throughput and better resource utilization, especially for I/O-bound applications.