package com.example.bootcamp.repository;

import com.example.bootcamp.entity.ExceptionLog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ExceptionLogRepository extends MongoRepository<ExceptionLog, String> {}
