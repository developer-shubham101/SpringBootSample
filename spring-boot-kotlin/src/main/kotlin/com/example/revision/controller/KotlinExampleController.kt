package com.example.revision.controller

import com.example.revision.sample.Address
import com.example.revision.sample.Rectangle
import com.example.revision.sample.greet
import com.example.revision.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/kotlin")
class KotlinExampleController(private val userService: UserService) {


    @GetMapping("/test")
    fun getUserById(): ResponseEntity<*> {


        val rectangle = Rectangle(3, 4)
        println("rectangle ${rectangle.area}")

        val address = Address()


        println("address $address")

        Address.getRandomValue() //accessing static function
        Address.RANDOM_VARIABLE //accessing static variable

        // Example of Extension Functions
        println("Shubham".greet())


        val add = { x: Int, y: Int -> x + y }

        add(2, 3)

        return ResponseEntity.ok(address)
    }

}
