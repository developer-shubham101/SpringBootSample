package in.newdevpoint.bootcamp.exceptions;

public class UserNotFoundException extends RuntimeException {
  /**
   * Constructs a new UserNotFoundException with the specified detail message.
   *
   * @param message the detail message explaining the reason for the exception
   */
  public UserNotFoundException(String message) {
    super(message);
  }
}
