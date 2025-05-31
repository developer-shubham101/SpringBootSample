package com.example.revision.service

import com.example.revision.entity.UserEntity
import com.example.revision.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {

    fun createUser(user: UserEntity): UserEntity = userRepository.save(user)

    fun getUserById(id: String): UserEntity? = userRepository.findById(id).orElse(null)

    fun updateUser(id: String, user: UserEntity): UserEntity? {
        return if (userRepository.existsById(id)) {
            val updatedUser = user.copy(id = id)
            userRepository.save(updatedUser)
        } else null
    }

    fun deleteUser(id: String): Boolean {
        return if (userRepository.existsById(id)) {
            userRepository.deleteById(id)
            true
        } else false
    }

    fun getAllUsers(): List<UserEntity> = userRepository.findAll()

}