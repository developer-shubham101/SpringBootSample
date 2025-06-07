package in.newdevpoint.bootcamp.service;

import com.github.rutledgepaulv.qbuilders.builders.GeneralQueryBuilder;
import com.github.rutledgepaulv.qbuilders.conditions.Condition;
import com.github.rutledgepaulv.qbuilders.visitors.MongoVisitor;
import com.github.rutledgepaulv.rqe.pipes.QueryConversionPipeline;
import in.newdevpoint.bootcamp.dto.UserReq;
import in.newdevpoint.bootcamp.entity.UserEntity;
import in.newdevpoint.bootcamp.mapper.UserMapper;
import in.newdevpoint.bootcamp.repository.UserRepository;
import in.newdevpoint.bootcamp.utility.FileUtility;
import java.io.File;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
@CrossOrigin(origins = "*")
public class UserService {

  private static final QueryConversionPipeline pipeline = QueryConversionPipeline.defaultPipeline();
  private final Environment environment;
  private final MongoTemplate template;
  private final UserMapper userMapper; // @RequiredArgsConstructor will create constructor
  private final UserRepository userRepository;

  /**
   * Searches for users with optional filtering, sorting, and pagination.
   *
   * <p>If a query string is provided, it is parsed into MongoDB criteria for advanced filtering.
   * Results can be sorted by a specified field and direction, and are paginated according to the
   * provided page and size parameters.
   *
   * @param size the number of users per page
   * @param page the page number to retrieve (zero-based)
   * @param sortDir the sort direction ("ASC" or "DESC"); defaults to ascending by "id" if not
   *     specified
   * @param query an optional query string for filtering users
   * @param sortBy the field to sort by; defaults to "id" if not specified
   * @return a paginated list of users matching the criteria
   */
  public Page<UserReq> searchUser(
      Integer size, Integer page, String sortDir, String query, String sortBy) {
    Criteria criteria;
    Query dynamicQuery;
    if (!StringUtils.isBlank(query)) {
      Condition<GeneralQueryBuilder> condition = pipeline.apply(query, UserEntity.class);
      criteria = condition.query(new MongoVisitor());

      dynamicQuery = new Query();
      dynamicQuery.addCriteria(criteria);

    } else {
      dynamicQuery = new Query();
    }

    long count = template.count(dynamicQuery, UserEntity.class);
    PageRequest pageable;
    if (!StringUtils.isBlank(sortDir) && !StringUtils.isBlank(sortBy)) {
      pageable = PageRequest.of(page, size, Sort.Direction.valueOf(sortDir.toUpperCase()), sortBy);
    } else {
      pageable = PageRequest.of(page, size, Sort.Direction.ASC, "id");
    }

    if (StringUtils.isBlank(sortBy)) {
      dynamicQuery.with(Sort.by(Sort.DEFAULT_DIRECTION, "id"));
    } else {
      dynamicQuery.with(Sort.by(sortBy));
    }
    dynamicQuery.with(pageable);

    List<UserEntity> orders = template.find(dynamicQuery, UserEntity.class);

    List<UserReq> users = userMapper.mapToResponseEntityList(orders);

    return PageableExecutionUtils.getPage(users, pageable, () -> count);
  }

  /**
   * Retrieves all user entities from the database.
   *
   * @return a list of all users
   */
  public List<UserEntity> getUsers() {
    return userRepository.findAll();
  }

  /**
   * Updates an existing user's information with the provided non-blank fields.
   *
   * <p>Only fields in {@code updatedUserEntity} that are not blank will overwrite the corresponding
   * fields of the user identified by {@code id}.
   *
   * @param id the unique identifier of the user to update
   * @param updatedUserEntity the user entity containing updated values
   * @return the updated user entity
   * @throws UsernameNotFoundException if no user exists with the given id
   */
  public UserEntity updateUser(String id, UserEntity updatedUserEntity) {
    return userRepository
        .findById(id)
        .map(
            existingUser -> {
              // Update all relevant fields only if provided
              if (StringUtils.isNotBlank(updatedUserEntity.getUsername())) {
                existingUser.setUsername(updatedUserEntity.getUsername());
              }
              if (StringUtils.isNotBlank(updatedUserEntity.getEmail())) {
                existingUser.setEmail(updatedUserEntity.getEmail());
              }
              // Add other fields as needed, e.g.:
              // if (StringUtils.isNotBlank(updatedUserEntity.getPhone())) {
              //     existingUser.setPhone(updatedUserEntity.getPhone());
              // }
              return userRepository.save(existingUser);
            })
        .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
  }

  /****
   * Deletes the user with the specified ID from the database.
   *
   * @param id the unique identifier of the user to delete
   */
  public void deleteUser(String id) {
    userRepository.deleteById(id);
  }

  /****
   * Retrieves a user by username.
   *
   * @param username the username to search for
   * @return an Optional containing the user if found, or empty if not found
   */
  public Optional<UserEntity> getUser(String username) {
    return userRepository.findByUsername(username);
  }

  /**
   * Retrieves a user by their unique identifier.
   *
   * @param id the unique identifier of the user
   * @return an Optional containing the UserEntity if found, or empty if not found
   */
  public Optional<UserEntity> getUserById(String id) {
    return userRepository.findById(id);
  }

  /**
   * Retrieves a user by username or throws an exception if not found.
   *
   * @param username the username to search for
   * @return the user entity matching the given username
   * @throws UsernameNotFoundException if no user with the specified username exists
   */
  public UserEntity loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository
        .findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("username not found"));
  }

  /**
   * Retrieves a user by their unique ID.
   *
   * @param id the unique identifier of the user
   * @return the user entity with the specified ID
   * @throws UsernameNotFoundException if no user with the given ID is found
   */
  public UserEntity findById(String id) {
    return userRepository
        .findById(id)
        .orElseThrow(() -> new UsernameNotFoundException("user id not found"));
  }

  /**
   * Uploads a profile image for the specified user and updates the user's profile with the image
   * URL.
   *
   * @param file the profile image file to upload
   * @param username the username of the user whose profile image is to be updated
   * @return the updated UserEntity with the new profile image URL, or null if the user does not
   *     exist
   */
  public UserEntity uploadUserProfile(MultipartFile file, String username) {
    String uploadPath = environment.getProperty("file.upload.path");
    // Create upload directory if it doesn't exist
    File uploadDir = new File(uploadPath);
    if (!uploadDir.exists()) {
      uploadDir.mkdirs();
    }

    String uploadedFileUrl = FileUtility.fileUpload(file, uploadPath);

    UserEntity existingUserEntity = userRepository.findByUsername(username).orElse(null);
    if (existingUserEntity != null) {
      existingUserEntity.setProfileImage(uploadedFileUrl);

      return userRepository.save(existingUserEntity);
    }
    return null;
  }
}
