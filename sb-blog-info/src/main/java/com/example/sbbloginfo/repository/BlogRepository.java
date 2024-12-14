package com.example.sbbloginfo.repository;

import com.example.sbbloginfo.entity.BlogEntity;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BlogRepository extends MongoRepository<BlogEntity, String> {
  List<BlogEntity> findAllByUserId(String userId);
}
