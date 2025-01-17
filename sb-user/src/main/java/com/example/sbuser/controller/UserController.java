package com.example.sbuser.controller;

import com.example.sbuser.entity.User;
import com.example.sbuser.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
@Validated
public class UserController {

  private final UserService userService;

  private final Logger logger = LoggerFactory.getLogger(UserController.class);

  @PostMapping
  public ResponseEntity<User> createUser(@RequestBody @Valid User user) {

    User newUser = userService.saveUser(user);
    return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
  }

  @GetMapping("/{userId}")
  public ResponseEntity<User> getSingleUser(@PathVariable String userId) {
    logger.info("Get Single User Handler: UserController");
    //        logger.info("Retry count: {}", retryCount);

    User user = userService.getUserAllDetails(userId);
    return ResponseEntity.ok(user);
  }

  @GetMapping
  public ResponseEntity<List<User>> getAllUser() {
    List<User> allUser = userService.getAllUser();
    return ResponseEntity.ok(allUser);
  }
}
