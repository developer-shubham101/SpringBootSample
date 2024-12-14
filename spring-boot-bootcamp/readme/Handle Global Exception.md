In Spring Boot, a global exception handler is a centralized way to manage exceptions across the whole application. It allows you to define a common response for various types of errors and exceptions that may occur during the execution of your application. The main approach is to use the `@ControllerAdvice` and `@ExceptionHandler` annotations to handle exceptions globally.

### Steps to Create a Global Exception Handler

#### 1. Create a Global Exception Handler Class

This class will contain methods to handle specific exceptions that may occur in the application. Annotate the class with `@ControllerAdvice` to indicate that it applies to all controllers globally.

```java
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Handle specific exception
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {
        
        // Custom response structure
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                request.getDescription(false)
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    // Handle general exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobalException(
            Exception ex, WebRequest request) {
        
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected error occurred",
                request.getDescription(false)
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
```

#### 2. Create a Custom Error Response Class

This class is used to structure the response that will be sent back when an exception occurs. You can add fields like timestamp, error message, error details, or status code to provide more context to the client.

```java
public class ErrorResponse {
    private int statusCode;
    private String message;
    private String details;

    public ErrorResponse(int statusCode, String message, String details) {
        this.statusCode = statusCode;
        this.message = message;
        this.details = details;
    }

    // Getters and Setters
    public int getStatusCode() {
        return statusCode;
    }
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getDetails() {
        return details;
    }
    public void setDetails(String details) {
        this.details = details;
    }
}
```

#### 3. Define Custom Exceptions (Optional)

If your application has specific requirements, you may want to create custom exceptions. For example, a `ResourceNotFoundException` for handling cases when a resource isn’t found.

```java
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
```

#### 4. Throw Custom Exceptions from Controllers or Services

To see the global exception handler in action, throw a custom exception like `ResourceNotFoundException` in a controller or service layer.

```java
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/resource")
public class ResourceController {

    @GetMapping("/{id}")
    public String getResourceById(@PathVariable String id) {
        // Simulate a scenario where resource is not found
        if ("invalid-id".equals(id)) {
            throw new ResourceNotFoundException("Resource not found with ID: " + id);
        }

        return "Resource data for ID " + id;
    }
}
```

### Explanation of Annotations Used

- **@ControllerAdvice**: Indicates that this class will handle exceptions globally across all controllers in the application.
- **@ExceptionHandler**: Specifies the type of exception the method will handle. You can have multiple methods to handle different exception types.
- **ResponseEntity**: Used to set the HTTP status code and the response body when returning the custom error response.

### Example Usage

If a request to `/api/resource/invalid-id` is made, the application will throw a `ResourceNotFoundException`, which will be caught by the `handleResourceNotFoundException` method in `GlobalExceptionHandler`. The client will receive a JSON response similar to:

```json
{
    "statusCode": 404,
    "message": "Resource not found with ID: invalid-id",
    "details": "uri=/api/resource/invalid-id"
}
```

### Optional Improvements

- **Logging**: You can log exceptions within each handler to keep a record of all errors for monitoring and debugging.
- **Additional Custom Handlers**: You can add more specific exception handlers for `IllegalArgumentException`, `BadRequestException`, etc.
- **Validation Exceptions**: For handling validation errors, consider using `MethodArgumentNotValidException` for customizing validation error responses.

### Benefits of Using Global Exception Handling

- **Centralized Error Handling**: All exceptions are handled in one place, making it easier to maintain and extend.
- **Consistent Responses**: Provides a standard error structure, which improves client-side error handling and debugging.
- **Enhanced Code Readability**: Makes controllers and services cleaner, as they do not need to handle exceptions directly.