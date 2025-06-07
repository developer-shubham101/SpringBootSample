package in.newdevpoint.bootcamp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import in.newdevpoint.bootcamp.dto.UserReq;
import in.newdevpoint.bootcamp.entity.UserEntity;
import in.newdevpoint.bootcamp.mapper.UserMapper;
import in.newdevpoint.bootcamp.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.multipart.MultipartFile;

/**
 * Test class for {@link UserService}. This class contains unit tests for all the methods in the
 * UserService class. It uses Mockito for mocking dependencies and JUnit Jupiter for testing.
 *
 * <p>Annotations used in this class:
 *
 * <p>{@code @ExtendWith(MockitoExtension.class)} - Enables Mockito annotations in the test class -
 * Integrates Mockito with JUnit 5's extension mechanism - Required for using Mockito annotations
 * like @Mock and @InjectMocks
 *
 * <p>{@code @Mock} - Creates mock objects for dependencies - Used for: Environment, MongoTemplate,
 * UserMapper, and UserRepository - Allows controlling the behavior of these dependencies in tests
 *
 * <p>{@code @InjectMocks} - Creates an instance of UserService - Automatically injects all @Mock
 * fields into the service - Used for the class under test (UserService)
 *
 * <p>{@code @BeforeEach} - Marks a method to be executed before each test method - Used for test
 * setup and initialization - In this class, initializes test data in setUp() method
 *
 * <p>{@code @Test} - Marks a method as a test method - Methods with this annotation will be
 * executed as tests - Used on all test methods in the class
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

  @Mock private Environment environment;

  @Mock private MongoTemplate template;

  @Mock private UserMapper userMapper;

  @Mock private UserRepository userRepository;

  @InjectMocks private UserService userService;

  private UserEntity userEntity;
  private UserReq userReq;

  /**
   * Sets up test data before each test method execution. Initializes a sample user entity and user
   * request object. This method is executed before each test method due to @BeforeEach annotation.
   */
  @BeforeEach
  void setUp() {
    userEntity = new UserEntity("testUser", "test@example.com", "123456");
    userEntity.setId("1");

    userReq = new UserReq();
    userReq.setId("1");
    userReq.setUsername("testUser");
    userReq.setEmail("test@example.com");
  }

  /**
   * Tests the searchUser method to verify it returns a paginated list of users. Verifies that the
   * method correctly handles pagination, sorting, and mapping of results.
   */
  @Test
  void searchUser_returnsPageOfUsers() {
    List<UserEntity> userEntities = new ArrayList<>();
    userEntities.add(userEntity);

    List<UserReq> userReqs = new ArrayList<>();
    userReqs.add(userReq);

    Page<UserEntity> page = new PageImpl<>(userEntities);
    when(template.count(any(), eq(UserEntity.class))).thenReturn((long) userEntities.size());
    when(template.find(any(), eq(UserEntity.class))).thenReturn(userEntities);
    when(userMapper.mapToResponseEntityList(any())).thenReturn(userReqs);

    Page<UserReq> result = userService.searchUser(10, 0, "ASC", null, "id");

    assertNotNull(result);
    assertEquals(1, result.getContent().size());
    assertEquals("testUser", result.getContent().get(0).getUsername());
    verify(template).count(any(), eq(UserEntity.class));
    verify(template).find(any(), eq(UserEntity.class));
    verify(userMapper).mapToResponseEntityList(any());
  }

  /**
   * Tests the getUsers method to verify it returns all users from the repository. Verifies that the
   * method correctly retrieves and returns the list of users.
   */
  @Test
  void getUsers_returnsAllUsers() {
    List<UserEntity> userEntities = new ArrayList<>();
    userEntities.add(userEntity);
    when(userRepository.findAll()).thenReturn(userEntities);

    List<UserEntity> result = userService.getUsers();

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("testUser", result.get(0).getUsername());
    verify(userRepository).findAll();
  }

  /**
   * Tests the updateUser method to verify it successfully updates an existing user. Verifies that
   * the method correctly updates user information and returns the updated user.
   */
  @Test
  void updateUser_updatesExistingUser() {
    UserEntity existingUser = new UserEntity("oldUser", "test@example.com", "123456");
    existingUser.setId("1");

    UserEntity updatedUser = new UserEntity("newUser", "test@example.com", "123456");
    updatedUser.setId("1");

    when(userRepository.findById("1")).thenReturn(Optional.of(existingUser));
    when(userRepository.save(any(UserEntity.class))).thenReturn(updatedUser);

    UserEntity result = userService.updateUser("1", updatedUser);

    assertNotNull(result);
    assertEquals("newUser", result.getUsername());
    assertEquals("test@example.com", result.getEmail());
    verify(userRepository).findById("1");
    verify(userRepository).save(any(UserEntity.class));
  }

  /**
   * Tests the updateUser method to verify it throws an exception when the user is not found.
   * Verifies that the method throws UsernameNotFoundException for non-existent users.
   */
  @Test
  void updateUser_throwsExceptionWhenUserNotFound() {
    UserEntity updatedUser = new UserEntity("newUser", "test@example.com", "123456");
    when(userRepository.findById("1")).thenReturn(Optional.empty());

    assertThrows(UsernameNotFoundException.class, () -> userService.updateUser("1", updatedUser));
    verify(userRepository).findById("1");
    verify(userRepository, never()).save(any(UserEntity.class));
  }

  /**
   * Tests the deleteUser method to verify it successfully deletes a user by ID. Verifies that the
   * method correctly calls the repository's delete method.
   */
  @Test
  void deleteUser_deletesUserById() {
    String userId = "1";
    userService.deleteUser(userId);
    verify(userRepository).deleteById(userId);
  }

  /**
   * Tests the getUser method to verify it returns a user by username. Verifies that the method
   * correctly retrieves and returns the user when found.
   */
  @Test
  void getUser_returnsUserByUsername() {
    when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(userEntity));

    Optional<UserEntity> result = userService.getUser("testUser");

    assertTrue(result.isPresent());
    assertEquals("testUser", result.get().getUsername());
    verify(userRepository).findByUsername("testUser");
  }

  /**
   * Tests the getUserById method to verify it returns a user by ID. Verifies that the method
   * correctly retrieves and returns the user when found.
   */
  @Test
  void getUserById_returnsUserById() {
    when(userRepository.findById("1")).thenReturn(Optional.of(userEntity));

    Optional<UserEntity> result = userService.getUserById("1");

    assertTrue(result.isPresent());
    assertEquals("testUser", result.get().getUsername());
    verify(userRepository).findById("1");
  }

  /**
   * Tests the loadUserByUsername method to verify it returns a user by username. Verifies that the
   * method correctly loads and returns the user for authentication.
   */
  @Test
  void loadUserByUsername_returnsUserByUsername() {
    when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(userEntity));

    UserEntity result = userService.loadUserByUsername("testUser");

    assertNotNull(result);
    assertEquals("testUser", result.getUsername());
    verify(userRepository).findByUsername("testUser");
  }

  /**
   * Tests the loadUserByUsername method to verify it throws an exception when user is not found.
   * Verifies that the method throws UsernameNotFoundException for non-existent users.
   */
  @Test
  void loadUserByUsername_throwsExceptionWhenUserNotFound() {
    when(userRepository.findByUsername("nonExistentUser")).thenReturn(Optional.empty());

    assertThrows(
        UsernameNotFoundException.class, () -> userService.loadUserByUsername("nonExistentUser"));
    verify(userRepository).findByUsername("nonExistentUser");
  }

  /**
   * Tests the findById method to verify it returns a user by ID. Verifies that the method correctly
   * retrieves and returns the user when found.
   */
  @Test
  void findById_returnsUserById() {
    when(userRepository.findById("1")).thenReturn(Optional.of(userEntity));

    UserEntity result = userService.findById("1");

    assertNotNull(result);
    assertEquals("testUser", result.getUsername());
    verify(userRepository).findById("1");
  }

  /**
   * Tests the findById method to verify it throws an exception when user is not found. Verifies
   * that the method throws UsernameNotFoundException for non-existent user IDs.
   */
  @Test
  void findById_throwsExceptionWhenUserNotFoundById() {
    when(userRepository.findById("nonExistentId")).thenReturn(Optional.empty());

    assertThrows(UsernameNotFoundException.class, () -> userService.findById("nonExistentId"));
    verify(userRepository).findById("nonExistentId");
  }

  /**
   * Tests the uploadUserProfile method to verify it successfully updates a user's profile. Verifies
   * that the method correctly handles file upload and updates the user's profile.
   */
  @Test
  void uploadUserProfile_updatesUserProfile() {
    MultipartFile file =
        new MockMultipartFile("file", "test.jpg", "image/jpeg", "test image content".getBytes());
    when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(userEntity));
    when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
    when(environment.getProperty("file.upload.path")).thenReturn("uploads/");

    UserEntity result = userService.uploadUserProfile(file, "testUser");

    assertNotNull(result);
    assertEquals("testUser", result.getUsername());
    verify(userRepository).findByUsername("testUser");
    verify(userRepository).save(any(UserEntity.class));
  }

  /**
   * Tests the uploadUserProfile method to verify it returns null when user is not found. Verifies
   * that the method handles the case when the user doesn't exist.
   */
  @Test
  void uploadUserProfile_returnsNullWhenUserNotFound() {
    MultipartFile file =
        new MockMultipartFile("file", "test.jpg", "image/jpeg", "test image content".getBytes());
    when(userRepository.findByUsername("testUser")).thenReturn(Optional.empty());

    UserEntity result = userService.uploadUserProfile(file, "testUser");

    assertNull(result);
    verify(userRepository).findByUsername("testUser");
    verify(userRepository, never()).save(any(UserEntity.class));
  }
}
