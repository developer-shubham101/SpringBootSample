package com.example.revision.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document


@Document(collection = "users")
data class UserEntity(
    @Id
    val id: String? = null,
    val name: String,
    val email: String,

) {
    init {
        println("Person created: Name = $name, Email = $email")
    }

}