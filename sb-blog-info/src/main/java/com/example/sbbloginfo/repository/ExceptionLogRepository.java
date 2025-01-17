package com.example.sbbloginfo.repository;

import com.example.sbbloginfo.entity.ExceptionLog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ExceptionLogRepository extends MongoRepository<ExceptionLog, String> {}
