package in.newdevpoint.bootcamp;

import static org.junit.jupiter.api.Assertions.*;

import in.newdevpoint.bootcamp.entity.ERole;
import in.newdevpoint.bootcamp.entity.Role;
import in.newdevpoint.bootcamp.entity.UserEntity;
import in.newdevpoint.bootcamp.service.OrderService;
import in.newdevpoint.bootcamp.service.ProductService;
import in.newdevpoint.bootcamp.service.SystemService;
import in.newdevpoint.bootcamp.service.UserService;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Test class for the Spring Boot application. This class contains comprehensive tests to verify
 * various components of the application.
 *
 * <p>Annotations used in this class:
 *
 * <p>{@code @SpringBootTest} - Marks the class as a Spring Boot test - Loads the full application
 * context - Used for integration testing - Configures the test environment similar to the
 * production environment
 *
 * <p>{@code @Test} - Marks a method as a test method - Methods with this annotation will be
 * executed as tests - Used on all test methods in the class
 */
@SpringBootTest
class FirstProjectApplicationTests {

  @Autowired private UserService userService;

  @Autowired private ProductService productService;

  @Autowired private OrderService orderService;

  @Autowired private SystemService systemService;

  /**
   * Tests that the application context loads successfully. This is a basic test to ensure the
   * Spring application context can be initialized.
   */
  @Test
  void contextLoads() {
    assertNotNull(userService, "UserService should be autowired");
    assertNotNull(productService, "ProductService should be autowired");
    assertNotNull(orderService, "OrderService should be autowired");
    assertNotNull(systemService, "SystemService should be autowired");
  }

  /**
   * Tests user creation and retrieval functionality. Verifies that a user can be created and then
   * retrieved with the same username.
   */
  @Test
  void testUserCreationAndRetrieval() {
    // Create a test user with roles
    Set<Role> roles = new HashSet<>();
    roles.add(new Role(ERole.ROLE_USER));

    UserEntity user = new UserEntity("testuser", "test@example.com", "password123");
    user.setRoles(roles);

    // Save the user
    UserEntity savedUser = userService.updateUser("new", user);
    assertNotNull(savedUser, "Saved user should not be null");
    assertEquals("testuser", savedUser.getUsername(), "Username should match");
    assertEquals("test@example.com", savedUser.getEmail(), "Email should match");

    // Retrieve the user
    UserEntity retrievedUser = userService.loadUserByUsername("testuser");
    assertNotNull(retrievedUser, "Retrieved user should not be null");
    assertEquals("testuser", retrievedUser.getUsername(), "Retrieved username should match");
    assertEquals("test@example.com", retrievedUser.getEmail(), "Retrieved email should match");
  }

  /**
   * Tests user not found exception handling. Verifies that attempting to retrieve a non-existent
   * user throws the appropriate exception.
   */
  @Test
  void testUserNotFound() {
    assertThrows(
        UsernameNotFoundException.class,
        () -> {
          userService.loadUserByUsername("nonexistentuser");
        },
        "Should throw UsernameNotFoundException for non-existent user");
  }

  /**
   * Tests external API integration. Verifies that the system can successfully fetch data from an
   * external API.
   */
  @Test
  void testExternalApiIntegration() {
    Object response = systemService.fetchExternalApi();
    assertNotNull(response, "External API response should not be null");
  }

  /**
   * Tests resource file reading functionality. Verifies that the system can successfully read a
   * resource file.
   */
  @Test
  void testResourceFileReading() {
    String fileContent = systemService.readFile();
    assertNotNull(fileContent, "File content should not be null");
    assertNotEquals("File not found", fileContent, "File should be found and read successfully");
  }

  /**
   * Tests order processing functionality. Verifies that order confirmation emails can be sent
   * asynchronously.
   */
  @Test
  void testOrderProcessing() {
    // This test verifies that the order service methods can be called without exceptions
    assertDoesNotThrow(
        () -> {
          orderService.sendOrderConfirmationEmail(new ArrayList<>(), "Test Order");
        },
        "Order processing should not throw exceptions");
  }

  /** Tests user role assignment. Verifies that roles can be properly assigned to users. */
  @Test
  void testUserRoleAssignment() {
    // Create a test user with admin role
    Set<Role> roles = new HashSet<>();
    roles.add(new Role(ERole.ROLE_ADMIN));

    UserEntity user = new UserEntity("adminuser", "admin@example.com", "adminpass");
    user.setRoles(roles);

    // Save the user
    UserEntity savedUser = userService.updateUser("new", user);
    assertNotNull(savedUser, "Saved user should not be null");
    assertTrue(
        savedUser.getRoles().stream().anyMatch(role -> role.getName() == ERole.ROLE_ADMIN),
        "User should have ROLE_ADMIN");
  }
}
