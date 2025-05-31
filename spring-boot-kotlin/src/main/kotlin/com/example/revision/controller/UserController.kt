package com.example.revision.controller

import com.example.revision.entity.UserEntity
import com.example.revision.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(private val userService: UserService) {

    @PostMapping
    fun createUser(@RequestBody user: UserEntity): ResponseEntity<UserEntity> {
        val createdUser = userService.createUser(user)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser)
    }

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: String): ResponseEntity<UserEntity> {
        val user = userService.getUserById(id)
        return if (user != null) ResponseEntity.ok(user)
        else ResponseEntity.notFound().build()
    }

    @PutMapping("/{id}")
    fun updateUser(@PathVariable id: String, @RequestBody user: UserEntity): ResponseEntity<UserEntity> {
        val updatedUser = userService.updateUser(id, user)
        return if (updatedUser != null) ResponseEntity.ok(updatedUser)
        else ResponseEntity.notFound().build()
    }

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: String): ResponseEntity<Void> {
        return if (userService.deleteUser(id)) ResponseEntity.noContent().build()
        else ResponseEntity.notFound().build()
    }

    @GetMapping
    fun getAllUsers(): ResponseEntity<List<UserEntity>> = ResponseEntity.ok(userService.getAllUsers())
}
