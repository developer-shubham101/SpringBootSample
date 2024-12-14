package com.example.sbuser.service;

import com.example.sbuser.entity.BlogEntity;
import com.example.sbuser.entity.User;
import com.example.sbuser.exceptions.ResourceNotFoundException;
import com.example.sbuser.exceptions.UserAlreadyExistsException;
import com.example.sbuser.repository.UserRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@AllArgsConstructor
@Service
public class UserService {

  private final UserRepository userRepository;

  private final RestTemplate restTemplate;

  private final Logger logger = LoggerFactory.getLogger(UserService.class);

  public User saveUser(User user) {
    // generate  unique userid
    //    String randomUserId = UUID.randomUUID().toString();
    //    user.setUserId(randomUserId);

    Optional<User> oldUser = userRepository.findByEmail(user.getEmail());

    if (oldUser.isPresent()) {
      throw new UserAlreadyExistsException(
          "User already exists with this email id : " + user.getEmail());
    }
    return userRepository.save(user);
  }

  public List<User> getAllUser() {
    // implement RATING SERVICE CALL: USING REST TEMPLATE
    return userRepository.findAll();
  }

  public User getUser(String userId) {

    return userRepository
        .findById(userId)
        .orElseThrow(
            () ->
                new ResourceNotFoundException(
                    "User with given id is not found on server !! : " + userId));
  }

  public User getUserAllDetails(String userId) {
    // get user from database with the help  of user repository
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        "User with given id is not found on server !! : " + userId));

    BlogEntity[] blogEntities =
        restTemplate.getForObject(
            "http://SB-BLOG-INFO/blogs/v1/info/user/" + user.getUserId(), BlogEntity[].class);
    logger.info("{} ", blogEntities);
    List<BlogEntity> blogEntityList = Arrays.stream(blogEntities).toList();

    user.setBlogs(blogEntityList);

    return user;
  }
}
