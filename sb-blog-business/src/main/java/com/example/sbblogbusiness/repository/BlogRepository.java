package com.example.sbblogbusiness.repository;

import com.example.sbblogbusiness.entity.BlogEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BlogRepository extends MongoRepository<BlogEntity, String> {}
