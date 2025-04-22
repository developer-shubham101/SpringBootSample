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

  public List<UserEntity> getUsers() {
    return userRepository.findAll();
  }

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

  public void deleteUser(String id) {
    userRepository.deleteById(id);
  }

  public Optional<UserEntity> getUser(String username) {
    return userRepository.findByUsername(username);
  }

  public Optional<UserEntity> getUserById(String id) {
    return userRepository.findById(id);
  }

  public UserEntity loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository
        .findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("username not found"));
  }

  public UserEntity findById(String id) {
    return userRepository
        .findById(id)
        .orElseThrow(() -> new UsernameNotFoundException("user id not found"));
  }

  public UserEntity uploadUserProfile(MultipartFile file, String username) {
    if (file == null || file.isEmpty()) {
      throw new IllegalArgumentException("File cannot be empty");
    }

    String absoluteFileUrl = environment.getProperty("file.absolute.url");
    if (StringUtils.isBlank(absoluteFileUrl)) {
      throw new IllegalStateException("File upload path not configured");
    }
    String uploadedFileUrl;
    try {
      uploadedFileUrl = FileUtility.fileUpload(file, absoluteFileUrl);
    } catch (Exception e) {
      throw new RuntimeException("Failed to upload file: " + e.getMessage(), e);
    }

    return userRepository
        .findByUsername(username)
        .map(
            user -> {
              user.setProfileImage(uploadedFileUrl);
              return userRepository.save(user);
            })
        .orElseThrow(
            () -> new UsernameNotFoundException("User not found with username: " + username));
  }
}
