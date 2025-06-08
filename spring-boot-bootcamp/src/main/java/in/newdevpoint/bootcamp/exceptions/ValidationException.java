package in.newdevpoint.bootcamp.exceptions;

/**
 * Custom exception for handling validation errors in the application. This exception is thrown when
 * input validation fails.
 */
public class ValidationException extends RuntimeException {
  public ValidationException(String message) {
    super(message);
  }

  public ValidationException(String message, Throwable cause) {
    super(message, cause);
  }
}
