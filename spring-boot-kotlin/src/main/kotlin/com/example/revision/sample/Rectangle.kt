package com.example.revision.sample

class Rectangle(private val width: Int, private val height: Int) {
    val area: Int // property type is optional since it can be inferred from the getter's return type
        get() = this.width * this.height


}