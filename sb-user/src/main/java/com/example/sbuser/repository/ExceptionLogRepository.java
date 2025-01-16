package com.example.sbuser.repository;

import com.example.sbuser.entity.ExceptionLog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ExceptionLogRepository extends MongoRepository<ExceptionLog, String> {}
