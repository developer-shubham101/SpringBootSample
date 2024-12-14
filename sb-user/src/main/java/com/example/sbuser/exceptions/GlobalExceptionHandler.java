package com.example.sbuser.exceptions;

import com.example.sbuser.payload.ApiResponse;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ApiResponse> handlerResourceNotFoundException(
      ResourceNotFoundException ex) {
    String message = ex.getMessage();
    ApiResponse response =
        ApiResponse.builder().message(message).success(true).status(HttpStatus.NOT_FOUND).build();
    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse> handlerException(Exception ex) {
    String message = ex.getMessage();
    ApiResponse response =
        ApiResponse.builder()
            .message(message)
            .success(true)
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .build();
    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
    String errorMessage =
        ex.getBindingResult().getFieldErrors().stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .collect(Collectors.joining(", "));
    ApiResponse response =
        ApiResponse.builder()
            .message(errorMessage)
            .success(true)
            .status(HttpStatus.BAD_REQUEST)
            .build();
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }
}
