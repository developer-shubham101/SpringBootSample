package com.example.sbuser.entity;

import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data // this annotation will create Getters and setters
@Document(collection = "users")
public class UserEntity {
  private String id;
  private String username;
  private String password;
  private String email;
  @DBRef private Set<Role> roles = new HashSet<>();

  public UserEntity(String username, String email, String password) {
    this.username = username;
    this.email = email;
    this.password = password;
  }
}
