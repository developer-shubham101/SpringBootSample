package in.newdevpoint.bootcamp;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import in.newdevpoint.bootcamp.dto.UserReq;
import in.newdevpoint.bootcamp.entity.UserEntity;
import in.newdevpoint.bootcamp.exceptions.UserNotFoundException;
import in.newdevpoint.bootcamp.mapper.UserMapper;
import in.newdevpoint.bootcamp.service.UserService;
import in.newdevpoint.bootcamp.usecase.UserUseCase;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserUseCaseTest {

  private final String userId = "67a3acfdf4051a6dea12e4a8";

  @Mock private UserService userService;

  //    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class); // ✅ Using real
  // MapStruct instance
  @Mock private UserMapper userMapper; // ✅ Mock UserMapper

  @InjectMocks private UserUseCase userUseCase;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this); // ✅ Ensures all mocks are initialized
  }

  @Test
  void testGetUserById_UserExists() {
    // Arrange
    UserEntity user = new UserEntity("John Doe", "john@example.com", "password");
    user.setId(userId);
    when(userService.getUserById(userId)).thenReturn(Optional.of(user));

    // Act
    UserReq result = userUseCase.getUserById(userId);

    // Assert
    assertNotNull(result);
    assertEquals("John Doe", result.getUsername());
  }

  @Test
  void testGetUserById_UserNotFound() {
    // Arrange
    when(userService.getUserById(userId)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(UserNotFoundException.class, () -> userUseCase.getUserById(userId));
  }

  @Test
  void testDeleteUser_UserExists() {
    // Arrange
    UserEntity user = new UserEntity("John Doe", "john@example.com", "password");
    user.setId(userId);
    when(userService.getUserById(userId)).thenReturn(Optional.of(user));
    doNothing().when(userService).deleteUser(userId);

    // Act
    userUseCase.deleteUser(userId);

    // Assert
    verify(userService, times(1)).deleteUser(userId);
  }

  @Test
  void testDeleteUser_UserNotFound() {
    // Arrange
    when(userService.getUserById(userId)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(UserNotFoundException.class, () -> userUseCase.deleteUser(userId));
  }

  @Test
  void testGetUserByUsername_UserExists() {
    // Arrange
    String username = "john_doe";
    UserEntity user = new UserEntity(username, "john@example.com", "password");
    when(userService.getUser(username)).thenReturn(Optional.of(user));

    // Act
    UserReq result = userUseCase.getUser(username);

    // Assert
    assertNotNull(result);
    assertEquals(username, result.getUsername());
  }

  @Test
  void testGetUserByUsername_UserNotFound() {
    // Arrange
    String username = "unknown_user";
    when(userService.getUser(username)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(UserNotFoundException.class, () -> userUseCase.getUser(username));
  }
}
