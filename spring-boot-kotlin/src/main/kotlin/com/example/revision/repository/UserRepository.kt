package com.example.revision.repository

import com.example.revision.entity.UserEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : MongoRepository<UserEntity, String>