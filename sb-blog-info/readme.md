# README - MongoDB, Spring Boot, and Docker

## Project Overview

This project is a Spring Boot application that integrates with MongoDB. It supports running both with and without Docker. Below are instructions for setting up the application in either environment.

---

## Requirements

- Java 11+
- Maven
- MongoDB (locally or via Docker)
- Docker (if using Docker setup)

---

## How to Run the Project

### **Option 1: Running Without Docker**

#### Step 1: Install and Set Up MongoDB

- If you choose not to use Docker, you'll need to manually install MongoDB on your system and start the MongoDB service.
- Once MongoDB is running, update the MongoDB configuration in the Spring Boot application.

Edit the `application.properties` file located in the `src/main/resources/` directory:

```properties
spring.data.mongodb.host=localhost
spring.data.mongodb.port=27017
spring.data.mongodb.database=ss_blog
```

#### Step 2: Run the Spring Boot Application Using Maven

Depending on your platform (Windows, macOS, Ubuntu), you can use the appropriate Maven wrapper command:

For **Windows**:

```bash
mvnw.cmd clean
mvnw.cmd install
mvnw.cmd spring-boot:run
```

For **macOS/Linux/Ubuntu**:

```bash
./mvnw clean
./mvnw install
./mvnw spring-boot:run
```

This will build and start the Spring Boot application.

---

### **Option 2: Running with Docker**

#### Step 1: Build and Run the Application with Docker

You can use Docker to run both MongoDB and the Spring Boot application. This is the easiest setup as it automatically configures MongoDB and the application environment.

To start the services, run the following Docker commands from the root of your project:

##### Build and Run Containers:
```bash
docker-compose up --build
```

##### Or run containers in detached mode:
```bash
docker-compose up -d
```

This will build the project, set up MongoDB, and start the Spring Boot application.

---

### API Testing

Once the Spring Boot application is up and running, visit the following URL to access the Swagger UI, where you can test all available APIs:

[Swagger UI - API Documentation](http://localhost:8080/swagger-ui/index.html#/)

---

## External API Integration

This application includes an additional feature that allows importing users from an external API into MongoDB.

### External API Information:
- External API: [http://localhost:8080/users/v1/](http://localhost:8080/users/v1/)
- Example Response:
  ```json
  [
    {
      "id": "001",
      "username": "shubham",
      "email": "shubham@example.com"
    }
  ]
  ```

### New Endpoint:
In the `UserController`, a new endpoint `importUsers` has been added to fetch all users from the external API and store them in the MongoDB database.

---

## Conclusion

This project can be run both using a local MongoDB setup or using Docker for easier management. The application is also capable of fetching and storing data from an external API into MongoDB. For API testing, Swagger UI is available for easy interaction with the app's endpoints.

