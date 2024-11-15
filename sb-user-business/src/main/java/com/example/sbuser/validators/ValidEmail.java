package com.example.sbuser.validators;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = EmailValidator.class) // Validator class
@Target({ElementType.FIELD, ElementType.PARAMETER}) // Can be used on fields and method parameters
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEmail {
  String message() default "Invalid email format";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
