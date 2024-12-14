package com.example.sbuser.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "users")
@Builder
public class User {

  @Id private String userId;

  @NotBlank(message = "Name cannot be empty")
  @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
  private String name;

  @Email(message = "Email should be valid")
  @NotBlank(message = "Email cannot be empty")
  private String email;

  private String about;
  // other user properties that you want

  private List<BlogEntity> blogs = new ArrayList<>();
}
