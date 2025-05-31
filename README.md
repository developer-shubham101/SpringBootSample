# SpringBootSample

## About the Project

Welcome to the **SpringBootSample** repository! This project serves as a comprehensive demo/example for beginners and intermediate developers who want to dive into **Spring Boot** development. It contains all the essential components, tools, and patterns required to build enterprise-level applications using Spring Boot.

The repository is structured to provide a solid foundation for learning and implementing best practices in building REST APIs, authentication, microservices, and more. Whether you're starting with the basics or looking to enhance your knowledge with advanced features, this project has something for everyone.

---

## Repository Overview

### Main Components

1. **spring-boot-bootcamp**
   - A self-contained project covering all the essential topics to get started with Spring Boot
   - Features include:
     - JWT Authentication
     - Basic REST API implementation
     - MongoDB integration
     - Docker support with docker-compose
     - Filebeat for centralized logging
     - OpenAPI documentation
     - Comprehensive documentation in the docs directory

2. **spring-boot-reactive**
   - Demonstrates Spring WebFlux and reactive programming patterns
   - Shows how to build non-blocking, event-driven applications
   - Includes reactive database interactions
   - Real-time data streaming examples

3. **spring-boot-hibernate**
   - Examples of JPA/Hibernate integration
   - Database relationship mappings and best practices
   - Entity management and transaction handling
   - Advanced querying techniques

4. **spring-boot-kotlin**
   - Spring Boot implementation using Kotlin programming language
   - Demonstrates Kotlin-specific features and best practices
   - Coroutines integration
   - Null safety features

---

## Features
- **Beginner-Friendly:** Covers the fundamental concepts for newcomers to Spring Boot
- **Enterprise-Grade Patterns:** Implements advanced concepts like reactive programming, containerization, and logging
- **Security:** Includes JWT-based authentication for secure APIs
- **Database Integration:** Demonstrates integration with MongoDB and relational databases
- **Containerization:** Docker support with docker-compose for easy deployment
- **Monitoring:** Filebeat integration for centralized logging
- **API Documentation:** OpenAPI/Swagger documentation for API endpoints
- **Multiple Learning Paths:** Separate modules for different aspects of Spring Boot development
- **IDE Support:** Includes configuration files for both IntelliJ IDEA and VS Code

---

## Full Documentation

Detailed documentation is available in the `spring-boot-bootcamp/docs` directory, covering various aspects of Spring Boot development:

### Documentation Topics
1. Spring Boot Fundamentals
2. Spring Boot Annotations
3. Spring Boot REST API
4. Spring Boot Security
5. Spring Boot Microservices
6. Spring Boot Docker
7. Spring Boot Advanced
8. Spring Boot Hibernate
9. Spring Boot Misc
10. Logging
11. Big O Notation

You can access the documentation through the following links:
- [Documentation on GitHub](https://github.com/developer-shubham101/SpringBootSample/tree/main/spring-boot-bootcamp/docs/docs.md)
- [Documentation on NewDevPoint](https://newdevpoint.in/spring-boot/)

---

## Getting Started

### Prerequisites
- **Java 8+** installed
- **Maven** installed
- **MongoDB** up and running
- **Docker** and **Docker Compose** (for containerized deployment)
- Basic understanding of Spring Boot and REST APIs
- IDE (IntelliJ IDEA or VS Code) with Spring Boot support

### Running the Project Locally

1. Clone the repository:
   ```bash
   git clone https://github.com/developer-shubham101/SpringBootSample.git
   ```

2. Navigate to the desired module (e.g., spring-boot-bootcamp):
   ```bash
   cd spring-boot-bootcamp
   ```

3. Using Maven:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

4. Using Docker:
   ```bash
   docker-compose up -d
   ```

The application will be available at `http://localhost:8080`. API documentation can be accessed at `http://localhost:8080/swagger-ui.html`.

### IDE Setup
- For IntelliJ IDEA: Import the project using the `.idea` configuration files
- For VS Code: Use the provided `.vscode` configuration files for optimal development experience
