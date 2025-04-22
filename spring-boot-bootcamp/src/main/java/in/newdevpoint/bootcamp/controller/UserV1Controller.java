package in.newdevpoint.bootcamp.controller;

import in.newdevpoint.bootcamp.api.UsersApi;
import in.newdevpoint.bootcamp.dto.UserReq;
import in.newdevpoint.bootcamp.usecase.UserUseCase;
import in.newdevpoint.bootcamp.utility.RoleConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
// @PreAuthorize(RoleConstants.USER_CRUD)
public class UserV1Controller implements UsersApi {
  private final UserUseCase userUseCase;

  /**
   * Retrieves user information by user ID.
   *
   * @param userId the unique identifier of the user to retrieve
   * @return HTTP 200 response containing the user's data
   */
  @Override
  @PreAuthorize(RoleConstants.ADMIN_CRUD)
  public ResponseEntity<UserReq> getUserById(String userId) {
    UserReq userProfile = userUseCase.getUserById(userId);
    return ResponseEntity.ok(userProfile);
  }

  /**
   * Retrieves the details of the currently authenticated user.
   *
   * @return HTTP 200 response containing the authenticated user's information
   */
  @Override
  @PreAuthorize(RoleConstants.USER_CRUD)
  public ResponseEntity<UserReq> getUserDetails() {

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String username = auth.getName();

    UserReq userProfile = userUseCase.getUser(username);

    return ResponseEntity.ok(userProfile);
  }

  /**
   * Searches for users with pagination, sorting, and optional query filtering.
   *
   * @param size the number of users per page
   * @param page the page number to retrieve
   * @param sortDir the direction of sorting ("asc" or "desc")
   * @param query an optional search query to filter users
   * @param sortBy the field to sort by
   * @return a ResponseEntity containing a paginated list of users with HTTP status 201 (Created)
   */
  @Override
  @PreAuthorize(RoleConstants.ADMIN_CRUD)
  public ResponseEntity<Object> searchUsers(
      Integer size, Integer page, String sortDir, String query, String sortBy) {
    Page<UserReq> userEntityList = userUseCase.searchUser(size, page, sortDir, query, sortBy);
    return ResponseEntity.status(HttpStatus.CREATED).body(userEntityList);
  }

  /**
   * Updates an existing user's information.
   *
   * <p>If the user is found and updated, returns the updated user data with HTTP 200 OK. If the
   * user does not exist, returns HTTP 404 Not Found.
   *
   * @param userEntity the user data to update
   * @return the updated user data if successful, or 404 Not Found if the user does not exist
   */
  @Override
  @PreAuthorize(RoleConstants.ADMIN_CRUD)
  public ResponseEntity<UserReq> updateUser(@RequestBody UserReq userEntity) {
    UserReq updatedUserEntity = userUseCase.updateUser(userEntity);
    if (updatedUserEntity != null) {
      return ResponseEntity.ok(updatedUserEntity);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Deletes a user by their unique identifier.
   *
   * <p>Returns HTTP 204 No Content upon successful deletion.
   *
   * @param id the unique identifier of the user to delete
   * @return a response entity with HTTP 204 status if deletion is successful
   */
  @Override
  @PreAuthorize(RoleConstants.ADMIN_CRUD)
  public ResponseEntity<Void> deleteUser(@PathVariable String id) {
    userUseCase.deleteUser(id);
    return ResponseEntity.noContent().build();
  }

  /**
   * Updates the authenticated user's profile photo.
   *
   * @param file the new profile photo to upload
   * @return the updated user information with the new profile photo
   */
  @Override
  @PreAuthorize(RoleConstants.USER_CRUD)
  public ResponseEntity<UserReq> updateUserProfilePhoto(MultipartFile file) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String username = auth.getName();
    UserReq updatedUserEntity = userUseCase.uploadUserProfile(file, username);

    return ResponseEntity.ok(updatedUserEntity);
  }
}
