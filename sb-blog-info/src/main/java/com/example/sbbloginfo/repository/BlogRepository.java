package com.example.sbbloginfo.repository;

import com.example.sbbloginfo.entity.BlogEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BlogRepository extends MongoRepository<BlogEntity, String> {}
