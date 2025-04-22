package in.newdevpoint.bootcamp.usecase;

import in.newdevpoint.bootcamp.dto.UserReq;
import in.newdevpoint.bootcamp.entity.UserEntity;
import in.newdevpoint.bootcamp.exceptions.UserNotFoundException;
import in.newdevpoint.bootcamp.mapper.UserMapper;
import in.newdevpoint.bootcamp.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserUseCase {

    private final UserMapper userMapper; // @RequiredArgsConstructor will create constructor
    @Autowired
    private UserService userService;

    /**
     * Invoked after the bean's properties have been initialized.
     *
     * Logs a message indicating that the bean has been initialized.
     */
    @PostConstruct
    public void init() {
        // Initialization logic here, e.g., open a file or validate configurations
        log.info("Bean has been initialized!");
    }

    /**
     * Performs cleanup operations before the bean is destroyed.
     */
    @PreDestroy
    public void cleanup() {
        // Cleanup logic here, e.g., close a connection or release resources
        log.info("Bean is about to be destroyed!");
    }

    /**
     * Retrieves all users and returns them as a list of user DTOs.
     *
     * @return a list of user request DTOs representing all users
     */
    public List<UserReq> getUsers() {
        List<UserEntity> userEntityList = userService.getUsers();
        return userMapper.mapToResponseEntityList(userEntityList);
    }

    /**
     * Searches for users with pagination, sorting, and optional query filtering.
     *
     * @param size    the number of users per page
     * @param page    the page number to retrieve
     * @param sortDir the direction of sorting ("asc" or "desc")
     * @param query   an optional search query to filter users
     * @param sortBy  the field to sort by
     * @return a paginated list of users matching the search criteria
     */
    public Page<UserReq> searchUser(
            Integer size, Integer page, String sortDir, String query, String sortBy) {
        return userService.searchUser(size, page, sortDir, query, sortBy);
    }

    /**
     * Updates an existing user with the provided user information.
     *
     * @param userReq the user data to update
     * @return the updated user as a UserReq DTO
     */
    public UserReq updateUser(UserReq userReq) {
        UserEntity userEntity = userMapper.mapFromReqToUserEntity(userReq);

        UserEntity updateUser = userService.updateUser(userEntity.getId(), userEntity);
        return userMapper.mapToResponseEntity(updateUser);
    }

    /**
     * Deletes a user with the specified ID.
     *
     * @param id the unique identifier of the user to delete
     */
    public void deleteUser(String id) {
        userService.deleteUser(id);
    }

    /**
     * Retrieves a user by username and returns its data as a UserReq DTO.
     *
     * @param username the username of the user to retrieve
     * @return the user data as a UserReq DTO
     * @throws UserNotFoundException if no user with the given username is found
     */
    public UserReq getUser(String username) {
        Optional<UserEntity> user = userService.getUser(username);
        return user.map(userMapper::mapToResponseEntity)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
    }

    /**
     * Retrieves a user by their unique ID.
     *
     * @param id the unique identifier of the user
     * @return the user data mapped to a UserReq DTO
     * @throws UserNotFoundException if no user is found with the given ID
     */
    public UserReq getUserById(String id) {
        Optional<UserEntity> user = userService.getUserById(id);
        return user.map(userMapper::mapToResponseEntity)
                .orElseThrow(() -> new UserNotFoundException("User not found with the given ID."));
    }

    /**
     * Updates a user's profile with the provided file and returns the updated user information.
     *
     * @param file the profile file to upload
     * @param username the username of the user whose profile is being updated
     * @return the updated user information as a UserReq DTO
     */
    public UserReq uploadUserProfile(MultipartFile file, String username) {
        UserEntity updateUser = userService.uploadUserProfile(file, username);

        return userMapper.mapToResponseEntity(updateUser);
    }
}
