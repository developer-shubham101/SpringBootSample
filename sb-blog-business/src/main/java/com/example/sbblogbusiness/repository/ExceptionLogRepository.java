package com.example.sbblogbusiness.repository;

import com.example.sbblogbusiness.entity.ExceptionLog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ExceptionLogRepository extends MongoRepository<ExceptionLog, String> {}
