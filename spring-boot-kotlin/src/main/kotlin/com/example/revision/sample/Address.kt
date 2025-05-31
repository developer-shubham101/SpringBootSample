package com.example.revision.sample

// Need to use open to make it as no final class
open class Address {

    // Static block in kotlin
    companion object {
        const val RANDOM_VARIABLE = "Random"
        fun getRandomValue() : String = RANDOM_VARIABLE
    }

    init{
        println("Address created")
    }

    var name: String = "Holmes, Sherlock"
    var street: String = "Baker"
    var city: String = "London"
    var state: String? = null
    var zip: String = "123456"
}

class UserAddress : Address() {

}