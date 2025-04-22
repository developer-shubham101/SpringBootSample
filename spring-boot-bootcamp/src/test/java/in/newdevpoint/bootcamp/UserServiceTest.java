package in.newdevpoint.bootcamp;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import in.newdevpoint.bootcamp.entity.UserEntity;
import in.newdevpoint.bootcamp.exceptions.UserNotFoundException;
import in.newdevpoint.bootcamp.repository.UserRepository;
import in.newdevpoint.bootcamp.service.UserService;
import java.util.List;
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

  @Test
  void testUpdateUser_UserExists() {
    UserEntity existingUser = new UserEntity("John Doe", "john@example.com", "password");
    existingUser.setId(userId);
    UserEntity updatedUser =
        new UserEntity("John Updated", "john.updated@example.com", "newpassword");
    updatedUser.setId(userId);

    when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
    when(userRepository.save(existingUser)).thenReturn(updatedUser);

    UserEntity result = userService.updateUser(userId, updatedUser);

    assertNotNull(result);
    assertEquals("John Updated", result.getUsername());
    assertEquals("john.updated@example.com", result.getEmail());
  }

  @Test
  void testUpdateUser_UserNotFound() {
    UserEntity updatedUser =
        new UserEntity("John Updated", "john.updated@example.com", "newpassword");

    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> userService.updateUser(userId, updatedUser));
  }

  @Test
  void testGetAllUsers() {
    UserEntity user1 = new UserEntity("John Doe", "john@example.com", "password");
    UserEntity user2 = new UserEntity("Jane Doe", "jane@example.com", "password");
    when(userRepository.findAll()).thenReturn(List.of(user1, user2));

    List<UserEntity> result = userService.getAllUsers();

    assertNotNull(result);
    assertEquals(2, result.size());
    assertEquals("John Doe", result.get(0).getUsername());
    assertEquals("Jane Doe", result.get(1).getUsername());
  }

  @Test
  void testGetAllUsers_EmptyList() {
    when(userRepository.findAll()).thenReturn(List.of());

    List<UserEntity> result = userService.getAllUsers();

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }
}
