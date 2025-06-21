# Spring Boot Hibernate Integration Project

This project demonstrates the integration of Spring Boot with Hibernate ORM, providing a robust foundation for building database-driven applications.

## Technologies Used

- Java 17
- Spring Boot 3.2.3
- Hibernate 6.4.4.Final
- Spring Data JPA
- H2 Database (for development)
- MySQL (for production)
- Lombok
- Maven

## Project Structure

The project follows a standard Maven layout:

```
spring-boot-hibernate
├── docs/
│   ├── mysql-docker-setup/
│   ├── sample-data.sql
│   └── sql-jpa-hibernate-guide.md
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/hibernatebootcamp/
│   │   │       ├── config/       # Spring Boot configuration
│   │   │       ├── controller/   # REST controllers
│   │   │       ├── dto/          # Data Transfer Objects
│   │   │       ├── entity/       # JPA entities
│   │   │       ├── exception/    # Custom exceptions
│   │   │       ├── mapper/       # MapStruct mappers
│   │   │       ├── repository/   # Spring Data repositories
│   │   │       └── service/      # Business logic
│   │   └── resources/   # `application.properties`, etc.
│   └── test/
├── pom.xml
└── openapi.yaml
```

## Features

- **RESTful APIs**: Exposes REST endpoints for managing users, blogs, and categories.
- **Spring Data JPA**: Simplifies data access layer with repository pattern.
- **Hibernate**: As the JPA provider for ORM.
- **Database Support**: Pre-configured for H2 (development) and MySQL (production).
- **DTOs and Mappers**: Uses Data Transfer Objects and MapStruct for clean architecture.
- **API Documentation**: Comes with an `openapi.yaml` file for Swagger/OpenAPI documentation.
- **Dockerized MySQL**: Includes a Docker Compose setup for an easy-to-run MySQL environment.

## Documentation

- **[SQL, JPA, and Hibernate Guide](./docs/sql-jpa-hibernate-guide.md)**: A comprehensive guide covering SQL basics, JPA, and Hibernate concepts.
- **[MySQL Setup Guide](./docs/mysql-docker-setup/how-to-setup-mysql.md)**: Detailed instructions for setting up MySQL for this project.
- **API Reference**: The API is documented in the `openapi.yaml` file, which can be used with tools like Swagger UI.
- **Swagger UI**: Access the interactive API documentation at [http://localhost:8080/hibernate/swagger-ui/index.html](http://localhost:8080/hibernate/swagger-ui/index.html)
- **Sample Data**: You can populate the database with sample data using [`sample-data.sql`](./docs/sample-data.sql).

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- MySQL (for production environment)

## Getting Started

1. Clone the repository:
   ```bash
   git clone [repository-url]
   cd spring-boot-hibernate
   ```

2. Build the project:
   ```bash
   mvn clean install
   ```

3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

The application will start on `http://localhost:8080` by default.

## Database Configuration

The project supports both H2 (in-memory) and MySQL databases:

- H2 Database (Development):
  - URL: `jdbc:h2:mem:testdb`
  - Username: `sa`
  - Password: (empty)

- MySQL (Production):
  - Configure your MySQL connection in `application.properties`

## MySQL Setup Options

For detailed instructions on setting up MySQL, refer to the [MySQL Setup Guide](./docs/mysql-docker-setup/how-to-setup-mysql.md).

### Option 1: Using Docker (Recommended)

1. Navigate to the `docs/mysql-docker-setup` directory:
   ```bash
   cd docs/mysql-docker-setup
   ```

2. Start the MySQL container:
   ```bash
   docker-compose up -d
   ```

This will start:
- MySQL server on port 3306
- phpMyAdmin on http://localhost:8081

Default credentials:
- Database: `hibernate_db`
- Username: `devuser`
- Password: `devpass`
- Root Password: `root`

### Option 2: Manual MySQL Installation

1. Install MySQL Server on your machine
2. Create a database named `hibernate_db`
3. Create a user with the following credentials:
   - Username: `devuser`
   - Password: `devpass`
4. Grant necessary privileges to the user

The application is pre-configured to use these credentials in `application.properties`. If you use different credentials, update the following properties:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/hibernate_db
spring.datasource.username=devuser
spring.datasource.password=devpass
```

## Dependencies

- `spring-boot-starter-web`: For building web applications
- `spring-boot-starter-data-jpa`: For JPA and Spring Data support
- `hibernate-core`: Core Hibernate ORM functionality
- `h2`: In-memory database for development
- `mysql-connector-j`: MySQL database driver
- `lombok`: Reduces boilerplate code
- `spring-boot-starter-test`: For testing support

## Building and Testing

To build the project:
```bash
mvn clean install
```

To run tests:
```bash
mvn test
```

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.
