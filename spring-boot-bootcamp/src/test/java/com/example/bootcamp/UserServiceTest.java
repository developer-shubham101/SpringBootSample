package com.example.bootcamp;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.bootcamp.entity.UserEntity;
import com.example.bootcamp.exceptions.UserNotFoundException;
import com.example.bootcamp.repository.UserRepository;
import com.example.bootcamp.service.UserService;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

  private final String userId = "67a3acfdf4051a6dea12e4a8";

  @Mock private UserRepository userRepository;

  @InjectMocks private UserService userService;

  @Test
  void testGetUserById_UserExists() {
    UserEntity user = new UserEntity("John Doe", "john@example.com", "password");
    user.setId(userId); // Ensure the user ID is set
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));

    Optional<UserEntity> result = userService.getUserById(userId);

    assertTrue(result.isPresent());

    assertEquals("John Doe", result.get().getUsername());
  }

  @Test
  void testGetUserById_UserNotFound() {
    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> userService.getUserById(userId));
  }

  @Test
  void testCreateUser() {
    UserEntity user = new UserEntity("Jane Doe", "jane@example.com", "password");
    UserEntity savedUser = new UserEntity("Jane Doe", "jane@example.com", "password");
    savedUser.setId(userId);
    when(userRepository.save(user)).thenReturn(savedUser);

    UserEntity result = userService.createUser(user);

    assertNotNull(result);
    assertEquals(userId, result.getId());
    assertEquals("Jane Doe", result.getUsername());
  }

  @Test
  void testDeleteUser_UserExists() {
    UserEntity user = new UserEntity("John Doe", "john@example.com", "password");
    user.setId(userId); // Ensure the user ID is set
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    doNothing().when(userRepository).deleteById(userId);

    userService.deleteUser(userId);

    verify(userRepository, times(1)).deleteById(userId);
  }

  @Test
  void testDeleteUser_UserNotFound() {
    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> userService.deleteUser(userId));
  }
}
