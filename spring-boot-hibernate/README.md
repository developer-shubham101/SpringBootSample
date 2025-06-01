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

```
src/
├── main/
│   ├── java/        # Java source files
│   └── resources/   # Configuration files
└── test/           # Test files
```

## Features

- Spring Boot Web for RESTful APIs
- Hibernate ORM for database operations
- Spring Data JPA for simplified data access
- Support for both H2 and MySQL databases
- Lombok for reducing boilerplate code

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

### Option 1: Using Docker (Recommended)

1. Navigate to the `mysql-docker-setup` directory:
   ```bash
   cd mysql-docker-setup
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
