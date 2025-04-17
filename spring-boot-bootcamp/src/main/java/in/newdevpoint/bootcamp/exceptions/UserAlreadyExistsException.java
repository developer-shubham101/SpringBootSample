package in.newdevpoint.bootcamp.exceptions;

public class UserAlreadyExistsException extends RuntimeException {
  /**
   * Constructs a new exception indicating that a user already exists.
   *
   * @param message the detail message explaining the exception
   */
  public UserAlreadyExistsException(String message) {
    super(message);
  }
}
