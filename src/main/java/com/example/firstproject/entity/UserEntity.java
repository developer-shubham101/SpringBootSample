package com.example.firstproject.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data // this annotation will create Getters and setters
@Document(collection = "users")
public class UserEntity {
  @Id private String id;
  private String username;
  private String email;
}
