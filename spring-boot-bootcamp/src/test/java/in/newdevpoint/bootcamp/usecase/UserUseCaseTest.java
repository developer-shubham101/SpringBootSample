package in.newdevpoint.bootcamp.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import in.newdevpoint.bootcamp.dto.UserReq;
import in.newdevpoint.bootcamp.entity.UserEntity;
import in.newdevpoint.bootcamp.exceptions.UserNotFoundException;
import in.newdevpoint.bootcamp.mapper.UserMapper;
import in.newdevpoint.bootcamp.service.UserService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

/**
 * Test class for {@link UserUseCase}. This class contains unit tests for all the methods in the
 * UserUseCase class. It uses Mockito for mocking dependencies and JUnit Jupiter for testing.
 *
 * <p>Annotations used in this class:
 *
 * <p>{@code @ExtendWith(MockitoExtension.class)} - Enables Mockito annotations in the test class -
 * Integrates Mockito with JUnit 5's extension mechanism - Required for using Mockito annotations
 * like @Mock and @InjectMocks
 *
 * <p>{@code @Mock} - Creates mock objects for dependencies - Used for: UserService and UserMapper -
 * Allows controlling the behavior of these dependencies in tests
 *
 * <p>{@code @InjectMocks} - Creates an instance of UserUseCase - Automatically injects all @Mock
 * fields into the use case - Used for the class under test (UserUseCase)
 *
 * <p>{@code @BeforeEach} - Marks a method to be executed before each test method - Used for test
 * setup and initialization - In this class, initializes test data in setUp() method
 *
 * <p>{@code @Test} - Marks a method as a test method - Methods with this annotation will be
 * executed as tests - Used on all test methods in the class
 */
@ExtendWith(MockitoExtension.class)
public class UserUseCaseTest {

  @Mock private UserService userService;

  @Mock private UserMapper userMapper;

  @InjectMocks private UserUseCase userUseCase;

  private UserEntity userEntity;
  private UserReq userReq;

  /**
   * Sets up test data before each test method execution. Initializes a sample user entity and user
   * request object. This method is executed before each test method due to @BeforeEach annotation.
   */
  @BeforeEach
  void setUp() {
    userEntity = new UserEntity("testUser", "test@example.com", "password123");
    userEntity.setId("1");

    userReq = new UserReq();
    userReq.setId("1");
    userReq.setUsername("testUser");
    userReq.setEmail("test@example.com");
  }

  /**
   * Tests the getUsers method to verify it returns a list of UserReq objects. Verifies that the
   * method correctly maps UserEntity objects to UserReq objects.
   */
  @Test
  void getUsers_returnsListOfUserReq() {
    List<UserEntity> userEntities = new ArrayList<>();
    userEntities.add(userEntity);
    when(userService.getUsers()).thenReturn(userEntities);
    when(userMapper.mapToResponseEntityList(userEntities)).thenReturn(List.of(userReq));

    List<UserReq> result = userUseCase.getUsers();

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("testUser", result.get(0).getUsername());
  }

  /**
   * Tests the searchUser method to verify it returns a paginated list of UserReq objects. Verifies
   * that the method correctly handles pagination and sorting.
   */
  @Test
  void searchUser_returnsPageOfUserReq() {
    Page<UserReq> mockPage = new PageImpl<>(List.of(userReq));
    when(userService.searchUser(10, 0, "ASC", null, "id")).thenReturn(mockPage);

    Page<UserReq> result = userUseCase.searchUser(10, 0, "ASC", null, "id");

    assertNotNull(result);
    assertEquals(1, result.getContent().size());
    assertEquals("testUser", result.getContent().get(0).getUsername());
  }

  /**
   * Tests the updateUser method to verify it successfully updates a user. Verifies that the method
   * correctly maps between UserReq and UserEntity objects.
   */
  @Test
  void updateUser_returnsUpdatedUserReq() {
    when(userMapper.mapFromReqToUserEntity(userReq)).thenReturn(userEntity);
    when(userService.updateUser("1", userEntity)).thenReturn(userEntity);
    when(userMapper.mapToResponseEntity(userEntity)).thenReturn(userReq);

    UserReq result = userUseCase.updateUser(userReq);

    assertNotNull(result);
    assertEquals("testUser", result.getUsername());
  }

  /**
   * Tests the deleteUser method to verify it successfully deletes a user. Verifies that the method
   * correctly calls the service layer.
   */
  @Test
  void deleteUser_deletesUser() {
    String userId = "1";
    userUseCase.deleteUser(userId);
    verify(userService, times(1)).deleteUser(userId);
  }

  /**
   * Tests the getUser method to verify it returns a UserReq object by username. Verifies that the
   * method correctly maps UserEntity to UserReq.
   */
  @Test
  void getUser_returnsUserReq() {
    when(userService.getUser("testUser")).thenReturn(Optional.of(userEntity));
    when(userMapper.mapToResponseEntity(userEntity)).thenReturn(userReq);

    UserReq result = userUseCase.getUser("testUser");

    assertNotNull(result);
    assertEquals("testUser", result.getUsername());
  }

  /**
   * Tests the getUser method to verify it throws an exception when user is not found. Verifies that
   * the method throws UserNotFoundException for non-existent users.
   */
  @Test
  void getUser_throwsExceptionWhenUserNotFound() {
    when(userService.getUser("nonExistentUser")).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> userUseCase.getUser("nonExistentUser"));
  }

  /**
   * Tests the getUserById method to verify it returns a UserReq object by ID. Verifies that the
   * method correctly maps UserEntity to UserReq.
   */
  @Test
  void getUserById_returnsUserReq() {
    when(userService.getUserById("1")).thenReturn(Optional.of(userEntity));
    when(userMapper.mapToResponseEntity(userEntity)).thenReturn(userReq);

    UserReq result = userUseCase.getUserById("1");

    assertNotNull(result);
    assertEquals("testUser", result.getUsername());
  }

  /**
   * Tests the getUserById method to verify it throws an exception when user is not found. Verifies
   * that the method throws UserNotFoundException for non-existent user IDs.
   */
  @Test
  void getUserById_throwsExceptionWhenUserNotFound() {
    when(userService.getUserById("nonExistentId")).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> userUseCase.getUserById("nonExistentId"));
  }

  /**
   * Tests the uploadUserProfile method to verify it successfully updates a user's profile. Verifies
   * that the method correctly handles file upload and mapping.
   */
  @Test
  void uploadUserProfile_returnsUpdatedUserReq() {
    MultipartFile file =
        new MockMultipartFile("file", "test.jpg", "image/jpeg", "test image content".getBytes());
    when(userService.uploadUserProfile(file, "testUser")).thenReturn(userEntity);
    when(userMapper.mapToResponseEntity(userEntity)).thenReturn(userReq);

    UserReq result = userUseCase.uploadUserProfile(file, "testUser");

    assertNotNull(result);
    assertEquals("testUser", result.getUsername());
  }
}
