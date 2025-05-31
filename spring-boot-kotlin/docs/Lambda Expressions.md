Here are several examples of lambda expressions in Kotlin to illustrate different use cases and syntactic styles.

### 1. **Basic Lambda Expression**
A lambda expression is defined using curly braces `{}` with parameters and the arrow `->` to separate parameters from the body.

```kotlin
val greet = { name: String -> "Hello, $name!" }
println(greet("Alice"))  // Output: Hello, Alice!
```

### 2. **Lambda as a Parameter in a Function**
Passing a lambda expression to a function to define custom behavior.

```kotlin
fun applyOperation(a: Int, b: Int, operation: (Int, Int) -> Int): Int {
    return operation(a, b)
}

val sum = applyOperation(5, 3) { x, y -> x + y }
println(sum)  // Output: 8
```

### 3. **Lambda Without Parameters**
A lambda with no parameters, often used for simple actions like printing.

```kotlin
val sayHello = { println("Hello!") }
sayHello()  // Output: Hello!
```

### 4. **Using `it` for Single Parameter**
When a lambda has only one parameter, Kotlin allows you to use `it` as an implicit name.

```kotlin
val square = { x: Int -> x * x }
println(square(4))  // Output: 16

val double = { it * 2 }  // `it` is the single parameter
println(double(5))  // Output: 10
```

### 5. **Lambda with Collection Functions**
Lambda expressions are commonly used with collection functions like `filter`, `map`, and `forEach`.

```kotlin
val numbers = listOf(1, 2, 3, 4, 5)

val evenNumbers = numbers.filter { it % 2 == 0 }
println(evenNumbers)  // Output: [2, 4]

val squaredNumbers = numbers.map { it * it }
println(squaredNumbers)  // Output: [1, 4, 9, 16, 25]
```

### 6. **Lambda with Default and Named Parameters**
Using default values and named parameters within a lambda expression.

```kotlin
val greetWithDefault = { name: String, greeting: String -> "$greeting, $name!" }
println(greetWithDefault("Alice", "Welcome"))  // Output: Welcome, Alice!
```

### 7. **Returning a Lambda from a Function**
A function that returns a lambda expression.

```kotlin
fun createMultiplier(factor: Int): (Int) -> Int {
    return { number -> number * factor }
}

val double = createMultiplier(2)
println(double(5))  // Output: 10
```

### 8. **Higher-Order Function with Lambda**
A function that takes a lambda as a parameter and invokes it.

```kotlin
fun execute(action: () -> Unit) {
    action()
}

execute { println("Executing action!") }  // Output: Executing action!
```

### 9. **Chaining Lambdas**
Lambdas can be combined with each other and chained.

```kotlin
val numbers = listOf(1, 2, 3, 4)
val result = numbers
    .filter { it > 1 }
    .map { it * 2 }
    .sum()

println(result)  // Output: 18
```

These examples showcase the versatility of lambda expressions in Kotlin, from basic transformations and functional programming to more complex operations and chaining.