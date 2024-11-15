To create a custom email validation in Spring Boot, you can follow these steps:

1. **Create a Custom Annotation for Email Validation**.
2. **Create a Validator Class to Implement the Validation Logic**.
3. **Use the Custom Annotation in Your Model Class**.

Let’s walk through the process step by step.

---

### **Step 1: Create a Custom Annotation**

First, you need to create a custom annotation that will be used to validate email addresses.

```java
import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = EmailValidator.class) // Validator class
@Target({ ElementType.FIELD, ElementType.PARAMETER }) // Can be used on fields and method parameters
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEmail {
    String message() default "Invalid email format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
```

### **Explanation**:
- `@Constraint`: Specifies the validator class (`EmailValidator`), which will contain the logic for validating the email.
- `@Target`: Specifies where this annotation can be applied (e.g., field, method parameter, etc.).
- `@Retention`: Specifies that the annotation should be available at runtime.

---

### **Step 2: Create the Validator Class**

Now, create a validator class that implements the `ConstraintValidator` interface to define the validation logic.

```java
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {

    private static final String EMAIL_PATTERN = 
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    private Pattern pattern;

    @Override
    public void initialize(ValidEmail constraintAnnotation) {
        pattern = Pattern.compile(EMAIL_PATTERN);
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null || email.isEmpty()) {
            return false; // Null or empty email is considered invalid
        }
        return pattern.matcher(email).matches(); // Validate against regex
    }
}
```

### **Explanation**:
- `ConstraintValidator<ValidEmail, String>`: This tells Spring that this validator will validate fields annotated with `@ValidEmail` and those fields will be of type `String`.
- `EMAIL_PATTERN`: A regular expression pattern that matches valid email addresses.
- `isValid`: The method where the actual validation logic happens. It returns `true` if the email is valid, `false` otherwise.

---

### **Step 3: Use the Custom Annotation in Your Model**

Now that you have the custom annotation and validator, you can use it in your model class.

```java
import javax.validation.constraints.NotBlank;

public class UserDTO {

    @NotBlank(message = "Email is mandatory")
    @ValidEmail // Applying the custom email validator
    private String email;

    // other fields, getters, setters, etc.
}
```

In this example, the `@ValidEmail` annotation is applied to the `email` field of `UserDTO`. This ensures that when the `UserDTO` object is validated, the email format will be checked.

---

### **Step 4: Enable Validation in the Controller**

You need to make sure that the Spring Boot application will actually trigger the validation. For this, you can use the `@Valid` annotation in your controller to ensure the input data is validated.

```java
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @PostMapping("/create")
    public String createUser(@Valid @RequestBody UserDTO userDTO) {
        // If validation passes, this method will execute
        return "User created successfully";
    }
}
```

### **Explanation**:
- `@Valid`: This annotation tells Spring to validate the incoming `UserDTO` object based on the annotations (like `@ValidEmail`) before the method executes.
- If the email is invalid, Spring will return an error response.

---

### **Step 5: Handle Validation Errors (Optional)**

If you want to customize the error response when validation fails, you can create a `@ControllerAdvice` to handle validation exceptions.

```java
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ValidationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errors.put(error.getField(), error.getDefaultMessage()));

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
```

### **Explanation**:
- This advice class listens for validation exceptions (`MethodArgumentNotValidException`).
- It then extracts the validation errors and returns a `400 Bad Request` response with a detailed error message.

---

### **Final Example Request and Response**

#### **Request**:
```http
POST /api/users/create
Content-Type: application/json

{
  "email": "invalid-email"
}
```

#### **Response**:
```json
{
  "email": "Invalid email format"
}
```

---

### **Summary**:

1. **Create a custom annotation** (`@ValidEmail`) that points to a validator.
2. **Create a validator class** (`EmailValidator`) that implements the validation logic.
3. **Use the custom annotation** in your model class (e.g., `UserDTO`).
4. **Enable validation** in your controller by using `@Valid`.
5. (Optional) Handle validation errors using `@ControllerAdvice`.

By following these steps, you can create custom validations like email format checks in a clean, reusable, and maintainable way.