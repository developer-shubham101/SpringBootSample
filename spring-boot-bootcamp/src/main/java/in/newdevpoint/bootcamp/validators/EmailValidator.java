package in.newdevpoint.bootcamp.validators;

import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {

  private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

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
