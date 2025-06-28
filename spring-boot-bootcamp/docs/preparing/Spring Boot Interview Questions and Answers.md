# Spring Boot Interview Questions and Answers

This document contains consolidated interview questions from the preparing folder with answers and links to relevant documentation.

---

## **Spring Boot Fundamentals & Annotations**

### Q1. @Component vs @Service vs @Repository
**Answer**: These are Spring stereotype annotations that serve different purposes:
- **@Component**: Generic stereotype for any Spring-managed component
- **@Service**: Specialization of @Component for business logic layer
- **@Repository**: Specialization of @Component for data access layer with automatic exception translation

**Reference**: `2. Spring Boot Annotations/2.6 Spring Boot Annotations - Differences Between Component Service Repository and Controller.md`

### Q2. What will happen if you will use @Service over a DAO?
**Answer**: Using @Service on a DAO class is not recommended. @Service should be used for business logic, while @Repository is designed for data access with automatic exception translation for persistence-related exceptions.

**Reference**: `2. Spring Boot Annotations/2.3 Spring Boot Annotations - Understanding Repository Annotation.md`

### Q3. Explain Spring Bean Lifecycle
**Answer**: Spring Bean Lifecycle includes instantiation, dependency injection, initialization (@PostConstruct), bean ready for use, and destruction (@PreDestroy).

**Reference**: `1. Spring Boot Fundamentals/1.6 Spring Boot Fundamentals - Understanding Bean Lifecycle.md`

### Q4. How you will call the @PreDestroy marked methods in the standalone application?
**Answer**: @PreDestroy methods are automatically called when the Spring context is closed. In standalone applications, this happens when the JVM shuts down or when context.close() is explicitly called.

**Reference**: `2. Spring Boot Annotations/2.8 Spring Boot Annotations - Spring PostConstruct and PreDestroy Annotations.md`

### Q5. Explain the helper class that initializes and destroys the web application context.
**Answer**: Spring Boot uses ApplicationContext to manage bean lifecycle. The context is initialized during application startup and destroyed during shutdown, automatically calling @PostConstruct and @PreDestroy methods.

**Reference**: `1. Spring Boot Fundamentals/1.6 Spring Boot Fundamentals - Understanding Bean Lifecycle.md`

### Q6. What are the RestClients you have used in your project?
**Answer**: Common REST clients in Spring Boot include:
- RestTemplate (synchronous)
- WebClient (asynchronous, reactive)
- Feign Client (declarative)
- Apache HttpClient

**Reference**: `3. Spring Boot REST API/3.1 Spring Boot REST API - Creating a REST API with Spring Boot.md`

### Q7. getForEntity() vs postForEntity()
**Answer**: 
- getForEntity(): Sends GET request and returns ResponseEntity with response body and status
- postForEntity(): Sends POST request with request body and returns ResponseEntity

**Reference**: `3. Spring Boot REST API/3.1 Spring Boot REST API - Creating a REST API with Spring Boot.md`

### Q8. What are the uses of ResponseEntity?
**Answer**: ResponseEntity provides control over HTTP response including status code, headers, and body. Useful for customizing responses and handling different HTTP status codes.

**Reference**: `3. Spring Boot REST API/3.1 Spring Boot REST API - Creating a REST API with Spring Boot.md`

### Q9. What should be the delete API method status code?
**Answer**: DELETE API should return:
- 204 No Content (if no response body)
- 200 OK (if returning some content)
- 404 Not Found (if resource doesn't exist)

**Reference**: `3. Spring Boot REST API/3.8 Spring Boot REST API - Best Practices for REST APIs in Spring Boot.md`

### Q10. Why should you handle response timeout while calling any API?
**Answer**: Response timeouts prevent applications from hanging indefinitely when external services are slow or unresponsive, improving system reliability and user experience.

**Reference**: `3. Spring Boot REST API/3.3 Spring Boot REST API - Handling Multiple Requests in Spring Boot.md`

### Q11. Explain the differences between Server timeout and Read timeout.
**Answer**: 
- Server timeout: Time to establish connection
- Read timeout: Time to wait for response after connection is established

**Reference**: `3. Spring Boot REST API/3.3 Spring Boot REST API - Handling Multiple Requests in Spring Boot.md`

### Q12. What is versioning in REST? What are the ways that you can use to implement versioning?
**Answer**: API versioning allows maintaining multiple versions of APIs. Common approaches:
- URL versioning (/api/v1/users)
- Header versioning (Accept: application/vnd.company.app-v1+json)
- Query parameter versioning (?version=1)

**Reference**: `3. Spring Boot REST API/3.8 Spring Boot REST API - Best Practices for REST APIs in Spring Boot.md`

### Q13. How does Basic Authentication work in REST API?
**Answer**: Basic Authentication encodes username:password in Base64 and sends in Authorization header. Server decodes and validates credentials.

**Reference**: `4. Spring Boot Security/4.1 Spring Boot Security - JWT Authentication and Authorization.md`

### Q14. Should you use JWT or Session-based authentication in the microservices environment?
**Answer**: JWT is preferred in microservices because it's stateless, scalable, and doesn't require session storage across services.

**Reference**: `4. Spring Boot Security/4.1 Spring Boot Security - JWT Authentication and Authorization.md`

### Q15. What is content negotiation?
**Answer**: Content negotiation allows clients to specify desired response format (JSON, XML) through headers, and server responds accordingly.

**Reference**: `3. Spring Boot REST API/3.1 Spring Boot REST API - Creating a REST API with Spring Boot.md`

### Q16. What are the cross-cutting concerns in spring? How do you implement it in microservices architecture?
**Answer**: Cross-cutting concerns include logging, security, transactions, caching. Implemented using AOP (Aspect-Oriented Programming) with aspects and advice.

**Reference**: `7. Spring Boot Advanced/7.1 Spring Boot Advanced - Aspect Oriented Programming AOP in Spring Boot.md`

### Q17. How to create custom validators in spring?
**Answer**: Create custom validators by implementing Validator interface or using @Constraint annotation with custom validation logic.

**Reference**: `3. Spring Boot REST API/3.7 Spring Boot REST API - Creating Custom Email Validation in Spring Boot.md`

---

## **Core Java Questions**

### Q18. Core Java interview questions and answers for experienced
**Answer**: TODO: Need to cover the topic

### Q19. How hashmap works internally?
**Answer**: HashMap uses array of buckets with hash function. Collisions handled by linked lists or balanced trees. Uses key's hashCode() and equals() methods.

**Reference**: TODO: Need to cover the topic

### Q20. List vs Set
**Answer**: List is ordered collection allowing duplicates, Set is unordered collection without duplicates.

**Reference**: TODO: Need to cover the topic

### Q21. Java interview questions coding
**Answer**: TODO: Need to cover the topic

### Q22. ArrayList Internal coding hands-on questions
**Answer**: TODO: Need to cover the topic

### Q23. Collection coding interview questions java
**Answer**: TODO: Need to cover the topic

### Q24. Defining the constructor capacity in ArrayList increases performance?
**Answer**: Yes, specifying initial capacity reduces resizing operations and improves performance when you know the approximate size.

**Reference**: TODO: Need to cover the topic

### Q25. How ArrayList works internally?
**Answer**: ArrayList uses dynamic array that grows by 50% when full. Internally manages array with size tracking.

**Reference**: TODO: Need to cover the topic

### Q26. Difference between fail-fast and fail-safe collections
**Answer**: Fail-fast throws ConcurrentModificationException on modification during iteration, fail-safe works on snapshot and doesn't throw exceptions.

**Reference**: TODO: Need to cover the topic

### Q27. Which types of collections consume more memory; fail-fast or fail-safe?
**Answer**: Fail-safe collections consume more memory as they maintain snapshots or copies of data.

**Reference**: TODO: Need to cover the topic

### Q28. Core Java coding questions in collection iterator
**Answer**: TODO: Need to cover the topic

### Q29. Can you modify the collection returned by Arrays.asList()?
**Answer**: You can modify elements but cannot add/remove elements as it returns fixed-size list backed by array.

**Reference**: TODO: Need to cover the topic

### Q30. Questions on ConcurrentModificationException and CopyOnWriteArrayList
**Answer**: ConcurrentModificationException occurs when collection is modified during iteration. CopyOnWriteArrayList creates new copy for modifications, avoiding this exception.

**Reference**: TODO: Need to cover the topic

### Q31. Explain the Singleton design pattern. How is Spring's singleton scope different from the GOF singleton?
**Answer**: Singleton ensures single instance per class. Spring's singleton is per Spring container, while GOF singleton is per JVM.

**Reference**: TODO: Need to cover the topic

### Q32. Give me a code walkthrough on the Spring Security internal workflow
**Answer**: Spring Security uses filter chain pattern. Request goes through authentication, authorization, and security filters before reaching application.

**Reference**: `4. Spring Boot Security/4.1 Spring Boot Security - JWT Authentication and Authorization.md`

---

## **Microservices Questions**

### Q33. @Controller Vs @RestController
**Answer**: @Controller returns view names, @RestController returns data directly (JSON/XML) for REST APIs.

**Reference**: `2. Spring Boot Annotations/2.7 Spring Boot Annotations - Differences Between RestController and Controller.md`

### Q34. How a spring boot application bootstraps?
**Answer**: Spring Boot uses @SpringBootApplication annotation which includes @Configuration, @EnableAutoConfiguration, and @ComponentScan to bootstrap the application.

**Reference**: `1. Spring Boot Fundamentals/1.1 Spring Boot Fundamentals - Introduction to Spring and Spring Boot.md`

### Q35. REST API Best practices
**Answer**: Use proper HTTP methods, status codes, versioning, authentication, error handling, and documentation.

**Reference**: `3. Spring Boot REST API/3.8 Spring Boot REST API - Best Practices for REST APIs in Spring Boot.md`

### Q36. Microservices interview questions
**Answer**: TODO: Need to cover the topic

### Q37. Monolithic Architecture Vs Microservices Architecture
**Answer**: Monolithic is single application, microservices are distributed services. Microservices offer scalability, flexibility but increase complexity.

**Reference**: `5. Spring Boot Microservices/5.7 Spring Boot Microservices - Overview of Microservice Architecture and Patterns.md`

### Q38. How to manage IPs and servers in a microservices architecture?
**Answer**: Use Service Discovery pattern with registries like Eureka or Consul to dynamically manage service locations.

**Reference**: `5. Spring Boot Microservices/5.1 Spring Boot Microservices - Service Discovery Pattern.md`

### Q39. How Service Discovery and Registration works? (A problem Scenario)
**Answer**: Services register with central registry on startup, send heartbeats, and clients query registry to find service instances.

**Reference**: `5. Spring Boot Microservices/5.1 Spring Boot Microservices - Service Discovery Pattern.md`

### Q40. How Client-side load balancing is different from Server-side load balancing?
**Answer**: Client-side load balancing distributes requests at client level, server-side uses central load balancer. Client-side reduces bottlenecks.

**Reference**: `5. Spring Boot Microservices/5.1 Spring Boot Microservices - Service Discovery Pattern.md`

### Q41. Is server-side load balancing a single point of failure?
**Answer**: Can be, but mitigated through redundancy, clustering, and failover mechanisms.

**Reference**: `5. Spring Boot Microservices/5.2 Spring Boot Microservices - API Gateway Pattern.md`

### Q42. How to handle failure in a microservices environment?
**Answer**: Use Circuit Breaker pattern, retry mechanisms, fallback strategies, and proper error handling.

**Reference**: `5. Spring Boot Microservices/5.5 Spring Boot Microservices - Circuit Breaker Pattern.md`

### Q43. How the retry pattern works?
**Answer**: Retry pattern automatically retries failed operations with exponential backoff to handle transient failures.

**Reference**: `5. Spring Boot Microservices/5.9 Spring Boot Microservices - Resilience4j for Fault Tolerance.md`

### Q44. How a block thread causes issues in a microservices environment?
**Answer**: Blocked threads can cause thread pool exhaustion, leading to service unavailability and cascading failures.

**Reference**: `5. Spring Boot Microservices/5.9 Spring Boot Microservices - Resilience4j for Fault Tolerance.md`

### Q45. How to protect your microservices from random load?
**Answer**: Use Rate Limiting pattern to restrict number of requests per client in given time period.

**Reference**: `5. Spring Boot Microservices/5.3 Spring Boot Microservices - Rate Limiting in Microservices.md`

### Q46. Circuit Breaker design pattern - Quick KT
**Answer**: Circuit Breaker prevents cascading failures by stopping requests to failing services and providing fallback responses.

**Reference**: `5. Spring Boot Microservices/5.5 Spring Boot Microservices - Circuit Breaker Pattern.md`

### Q47. Explain the Half-open state of a circuit breaker in spring boot
**Answer**: Half-open state allows limited test requests to check if service has recovered before fully opening circuit.

**Reference**: `5. Spring Boot Microservices/5.5 Spring Boot Microservices - Circuit Breaker Pattern.md`

### Q48. How to track a slower Microservice which causes performance issues?
**Answer**: Use monitoring tools, distributed tracing, and metrics to identify performance bottlenecks.

**Reference**: `7. Spring Boot Advanced/7.2 Spring Boot Advanced - Actuator Confirmations Explained.md`

### Q49. How to track exceptions in a microservices environment?
**Answer**: Use centralized logging, distributed tracing, and monitoring tools to track exceptions across services.

**Reference**: `7. Spring Boot Advanced/7.2 Spring Boot Advanced - Actuator Confirmations Explained.md`

### Q50. Entity Object Vs Value Object - When to use what?
**Answer**: Entity has unique identity, Value Object is defined by attributes. Use Entity for business objects with identity, Value Object for immutable data.

**Reference**: `8. Spring Boot Hibernate/8.3 Spring Boot Hibernate - Introduction to Hibernate.md`

### Q51. What is one-to-one mapping and how to set it up with JPA/Hibernate?
**Answer**: One-to-one mapping associates one entity with another. Use @OneToOne annotation with join columns.

**Reference**: `8. Spring Boot Hibernate/8.4 Spring Boot Hibernate - Understanding Object Relational Mapping ORM.md`

### Q52. What is an exchange in RabbitMQ?
**Answer**: Exchange is routing mechanism in RabbitMQ that directs messages to queues based on routing keys and bindings.

**Reference**: TODO: Need to cover the topic

---

## **Summary**

This document consolidates questions from the preparing folder with answers and references to existing documentation. Topics marked as "TODO: Need to cover the topic" require additional documentation to be created. 