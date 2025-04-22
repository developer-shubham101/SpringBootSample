package in.newdevpoint.bootcamp.repository;

import in.newdevpoint.bootcamp.entity.UserEntity;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<UserEntity, String> {

  /**
   * Retrieves a user entity by its username.
   *
   * @param username the username to search for
   * @return an Optional containing the user entity if found, or empty if not found
   */
  Optional<UserEntity> findByUsername(String username);

  /**
   * Checks if a user with the specified username exists in the database.
   *
   * @param username the username to check for existence
   * @return true if a user with the given username exists, false otherwise
   */
  Boolean existsByUsername(String username);

  /**
   * Checks if a user with the specified email exists in the database.
   *
   * @param email the email address to check for existence
   * @return true if a user with the given email exists, false otherwise
   */
  Boolean existsByEmail(String email);
}
