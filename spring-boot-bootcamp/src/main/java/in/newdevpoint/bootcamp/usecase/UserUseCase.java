package in.newdevpoint.bootcamp.usecase;

import in.newdevpoint.bootcamp.dto.UserReq;
import in.newdevpoint.bootcamp.entity.UserEntity;
import in.newdevpoint.bootcamp.exceptions.UserNotFoundException;
import in.newdevpoint.bootcamp.mapper.UserMapper;
import in.newdevpoint.bootcamp.service.UserService;
import java.util.List;
import java.util.Optional;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * UserUseCase class implements the Use Case pattern for user-related operations.
 * This class acts as an intermediary between the controller and service layers,
 * handling business logic and orchestrating operations.
 * 
 * Key responsibilities:
 * - Orchestrates user-related business operations
 * - Transforms data between DTOs and entities
 * - Handles business rules and validations
 * - Manages user lifecycle operations
 * 
 * The class uses constructor injection through @RequiredArgsConstructor
 * to ensure all dependencies are properly initialized.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserUseCase {

  /**
   * UserMapper for converting between DTOs and entities.
   * Injected via constructor (created by @RequiredArgsConstructor)
   */
  private final UserMapper userMapper;

  /**
   * UserService for handling core user operations.
   * Injected via constructor (created by @RequiredArgsConstructor)
   */
  private final UserService userService;

  /**
   * Lifecycle method called after dependency injection is complete.
   * This is where you can perform any initialization that requires
   * the injected dependencies to be ready.
   */
  @PostConstruct
  public void init() {
    log.info("UserUseCase: Bean initialization complete - All dependencies are ready");
  }

  /**
   * Lifecycle method called before the bean is destroyed.
   * Use this for cleanup operations like releasing resources
   * or closing connections.
   */
  @PreDestroy
  public void cleanup() {
    log.info("UserUseCase: Performing cleanup before bean destruction");
  }

  /**
   * Retrieves all users from the system.
   * This operation:
   * 1. Fetches all user entities from the service layer
   * 2. Maps them to DTOs for the presentation layer
   * 
   * @return List of UserReq DTOs containing user information
   */
  public List<UserReq> getUsers() {
    log.debug("Fetching all users");
    List<UserEntity> userEntityList = userService.getUsers();
    return userMapper.mapToResponseEntityList(userEntityList);
  }

  /**
   * Performs a paginated search for users with filtering and sorting.
   * This operation supports:
   * - Pagination (size and page number)
   * - Sorting (direction and field)
   * - Filtering (search query)
   * 
   * @param size Number of items per page
   * @param page Page number (0-based)
   * @param sortDir Sort direction ("asc" or "desc")
   * @param query Optional search query to filter users
   * @param sortBy Field to sort by
   * @return Page of UserReq DTOs matching the search criteria
   */
  public Page<UserReq> searchUser(
      Integer size, Integer page, String sortDir, String query, String sortBy) {
    log.debug("Searching users with pagination and filters");
    return userService.searchUser(size, page, sortDir, query, sortBy);
  }

  /**
   * Updates an existing user's information.
   * This operation:
   * 1. Converts the request DTO to an entity
   * 2. Updates the user in the service layer
   * 3. Converts the updated entity back to a DTO
   * 
   * @param userReq User data to update
   * @return Updated user information as UserReq DTO
   */
  public UserReq updateUser(UserReq userReq) {
    log.debug("Updating user with ID: {}", userReq.getId());
    UserEntity userEntity = userMapper.mapFromReqToUserEntity(userReq);
    UserEntity updateUser = userService.updateUser(userEntity.getId(), userEntity);
    return userMapper.mapToResponseEntity(updateUser);
  }

  /**
   * Deletes a user from the system.
   * This operation permanently removes the user and all associated data.
   * 
   * @param id ID of the user to delete
   */
  public void deleteUser(String id) {
    log.debug("Deleting user with ID: {}", id);
    userService.deleteUser(id);
  }

  /**
   * Retrieves a user by their username.
   * This operation:
   * 1. Searches for the user by username
   * 2. Throws UserNotFoundException if not found
   * 3. Maps the found user to a DTO
   * 
   * @param username Username to search for
   * @return User information as UserReq DTO
   * @throws UserNotFoundException if no user is found with the given username
   */
  public UserReq getUser(String username) {
    log.debug("Fetching user by username: {}", username);
    Optional<UserEntity> user = userService.getUser(username);
    return user.map(userMapper::mapToResponseEntity)
        .orElseThrow(() -> new UserNotFoundException("User not found."));
  }

  /**
   * Retrieves a user by their unique ID.
   * This operation:
   * 1. Searches for the user by ID
   * 2. Throws UserNotFoundException if not found
   * 3. Maps the found user to a DTO
   * 
   * @param id Unique identifier of the user
   * @return User information as UserReq DTO
   * @throws UserNotFoundException if no user is found with the given ID
   */
  public UserReq getUserById(String id) {
    log.debug("Fetching user by ID: {}", id);
    Optional<UserEntity> user = userService.getUserById(id);
    return user.map(userMapper::mapToResponseEntity)
        .orElseThrow(() -> new UserNotFoundException("User not found with the given ID."));
  }

  /**
   * Updates a user's profile with a new file.
   * This operation:
   * 1. Uploads the profile file
   * 2. Updates the user's profile information
   * 3. Returns the updated user information
   * 
   * @param file Profile file to upload
   * @param username Username of the user whose profile is being updated
   * @return Updated user information as UserReq DTO
   */
  public UserReq uploadUserProfile(MultipartFile file, String username) {
    log.debug("Uploading profile for user: {}", username);
    UserEntity updateUser = userService.uploadUserProfile(file, username);
    return userMapper.mapToResponseEntity(updateUser);
  }
}
