To use MongoDB with a Spring Boot application in Docker, you'll need to containerize both your Spring Boot application and MongoDB. You can achieve this either by running the containers independently or by using Docker Compose to manage both services together.

Here’s a complete guide to using MongoDB with Spring Boot in Docker:

---

## **Step-by-Step Guide**

### **1. Set Up MongoDB in Docker**

You can run MongoDB as a Docker container by using the official MongoDB Docker image. You can either pull the image and run it separately or use Docker Compose to orchestrate both MongoDB and your Spring Boot app in a multi-container setup.

#### **Option 1: Run MongoDB as a Standalone Docker Container**

```bash
docker run -d -p 27017:27017 --name mongo mongo
```

- This command pulls the MongoDB image from Docker Hub and runs it as a container.
- `-d`: Runs the container in the background.
- `-p 27017:27017`: Maps MongoDB's default port (27017) to the host's port 27017.
- `--name mongo`: Names the container `mongo`.

#### **Option 2: Use Docker Compose to Set Up Both Spring Boot and MongoDB**

Docker Compose allows you to manage both MongoDB and the Spring Boot application in a single configuration.

1. **Create a `docker-compose.yml` file in your project directory**:

```yaml
version: '3.8'

services:
  mongo:
    image: mongo:latest
    container_name: mongo
    ports:
      - 27017:27017
    volumes:
      - mongo-data:/data/db
    networks:
      - springboot-mongo-network

  springboot-app:
    image: my-spring-boot-app
    build:
      context: .
      dockerfile: Dockerfile
    depends_on:
      - mongo
    environment:
      SPRING_DATA_MONGODB_URI: mongodb://mongo:27017/mydatabase
    ports:
      - 8080:8080
    networks:
      - springboot-mongo-network

volumes:
  mongo-data:

networks:
  springboot-mongo-network:
```

- `mongo`: Sets up the MongoDB container.
  - **Image**: Uses the `mongo` Docker image.
  - **Ports**: Exposes port `27017`.
  - **Volumes**: Creates a volume to persist MongoDB data.
- `springboot-app`: Sets up the Spring Boot application container.
  - **Build**: Builds the Spring Boot app image from the `Dockerfile`.
  - **depends_on**: Ensures the `mongo` service starts before the Spring Boot app.
  - **Environment**: Sets the MongoDB URI for the Spring Boot app (`mongodb://mongo:27017/mydatabase`).

2. **Run Docker Compose**:

```bash
docker-compose up --build
```

This command builds the Spring Boot image (if not already built) and starts both the MongoDB and Spring Boot containers.

---

### **2. Add MongoDB Dependencies to Spring Boot**

In your Spring Boot project, you need to add Spring Data MongoDB dependencies.

1. **Add the MongoDB dependency in `pom.xml`** for Maven:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-mongodb</artifactId>
</dependency>
```

For Gradle, add the following to `build.gradle`:

```groovy
implementation 'org.springframework.boot:spring-boot-starter-data-mongodb'
```

---

### **3. Configure MongoDB in Spring Boot**

Spring Boot automatically connects to MongoDB using the default URI (`mongodb://localhost:27017`). Since MongoDB is running inside a Docker container, you'll need to configure the connection URL.

1. **In `application.properties` or `application.yml`**:

For `application.properties`:

```properties
spring.data.mongodb.uri=mongodb://mongo:27017/mydatabase
```

For `application.yml`:

```yaml
spring:
  data:
    mongodb:
      uri: mongodb://mongo:27017/mydatabase
```

Here, `mongo` is the hostname of the MongoDB service (as defined in the Docker Compose file), and `mydatabase` is the name of the MongoDB database.

### **4. Create a Spring Boot MongoDB Repository**

Now, create a simple repository to interact with MongoDB.

1. **Create a MongoDB entity**:

```java
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String name;
    private String email;

    // Getters and setters
}
```

2. **Create a repository interface**:

```java
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
}
```

3. **Create a controller to interact with MongoDB**:

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }
}
```

---

### **5. Build and Run the Application with Docker Compose**

Now that everything is set up, run your Spring Boot application and MongoDB using Docker Compose.

1. **Run Docker Compose**:

```bash
docker-compose up --build
```

2. **Access the Spring Boot app** at `http://localhost:8080`.

3. **Test MongoDB Integration** by sending HTTP requests to the `/users` endpoint (for example, using Postman or `curl`):

   - **Create a new user**:
     ```bash
     curl -X POST http://localhost:8080/users -H "Content-Type: application/json" -d '{"name": "John Doe", "email": "john@example.com"}'
     ```

   - **Retrieve all users**:
     ```bash
     curl http://localhost:8080/users
     ```

---

### **6. Clean Up**

If you want to stop the containers:

```bash
docker-compose down
```

---

### **Best Practices and Considerations**

- **Persisting MongoDB Data**: The `docker-compose.yml` file uses a volume (`mongo-data`) to ensure that MongoDB data persists across container restarts.
- **Container Dependencies**: The `depends_on` directive ensures MongoDB starts before the Spring Boot app, but Spring Boot will automatically retry connecting to MongoDB if it's not ready.
- **Environment-Specific Configurations**: Use Docker Compose profiles or `application.yml` profiles to handle environment-specific configurations for development, testing, and production.

By following these steps, you can successfully run your Spring Boot application with MongoDB using Docker and Docker Compose. This approach ensures that both services run in isolated environments and can be easily deployed or scaled.