package com.example.firstproject.service;

import com.example.firstproject.dto.UserReq;
import com.example.firstproject.entity.UserEntity;
import com.example.firstproject.mapper.UserMapper;
import com.example.firstproject.repository.UserRepository;
import com.github.rutledgepaulv.qbuilders.builders.GeneralQueryBuilder;
import com.github.rutledgepaulv.qbuilders.conditions.Condition;
import com.github.rutledgepaulv.qbuilders.visitors.MongoVisitor;
import com.github.rutledgepaulv.rqe.pipes.QueryConversionPipeline;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

@RequiredArgsConstructor
@Service
@CrossOrigin(origins = "*")
public class UserService {
  private final MongoTemplate template;
  private final UserMapper userMapper; // @RequiredArgsConstructor will create constructor
  private static final QueryConversionPipeline pipeline = QueryConversionPipeline.defaultPipeline();

  @Autowired private UserRepository userRepository;

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

    List<UserReq> users = userMapper.mapToResponseBeans(orders);

    return PageableExecutionUtils.getPage(users, pageable, () -> count);
  }

  public List<UserEntity> getUsers() {
    return userRepository.findAll();
  }

  public UserEntity createUser(UserEntity userEntity) {
    return userRepository.save(userEntity);
  }

  public UserEntity updateUser(String id, UserEntity updatedUserEntity) {
    UserEntity existingUserEntity = userRepository.findById(id).orElse(null);
    if (existingUserEntity != null) {
      existingUserEntity.setUsername(updatedUserEntity.getUsername());
      existingUserEntity.setEmail(updatedUserEntity.getEmail());
      return userRepository.save(existingUserEntity);
    }
    return null; // User not found
  }

  public void deleteUser(String id) {
    userRepository.deleteById(id);
  }
}
