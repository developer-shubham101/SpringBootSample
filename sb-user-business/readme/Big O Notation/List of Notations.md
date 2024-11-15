Here are examples of each common Big O notation represented with Java `for` loops. These examples showcase different growth rates based on the structure of the loops.

### 1. **O(1) - Constant Time**
This is a constant-time example, where the code runs in the same amount of time regardless of the size of the input.

```java
void constantTime(int[] array) {
    System.out.println(array[0]); // Accessing an element by index is O(1)
}
```

### 2. **O(log n) - Logarithmic Time**
Logarithmic time often involves halving the input size each time through the loop, like in binary search.

```java
void logarithmicTime(int n) {
    for (int i = 1; i < n; i *= 2) { // i doubles each iteration
        System.out.println("Logarithmic time step");
    }
}
```

In this example, the loop executes approximately \( \log_2(n) \) times because `i` doubles each time.

### 3. **O(n) - Linear Time**
Linear time complexity means the loop runs once for each element in the input.

```java
void linearTime(int[] array) {
    for (int i = 0; i < array.length; i++) {
        System.out.println("Linear time step for element " + array[i]);
    }
}
```

The runtime is proportional to `n`, where `n` is the length of the array.

### 4. **O(n log n) - Log-Linear Time**
Log-linear time complexity is common in efficient sorting algorithms, such as merge sort or quicksort. This can be represented with a loop that performs \( O(\log n) \) work for each of \( n \) elements.

```java
void logLinearTime(int n) {
    for (int i = 0; i < n; i++) { // Runs n times
        for (int j = 1; j < n; j *= 2) { // Inner loop runs log n times
            System.out.println("Log-linear time step");
        }
    }
}
```

The inner loop executes \( O(\log n) \) times, and the outer loop runs \( O(n) \) times, so the overall complexity is \( O(n \log n) \).

### 5. **O(n^2) - Quadratic Time**
Quadratic time complexity often arises from nested loops where each loop runs `n` times.

```java
void quadraticTime(int n) {
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
            System.out.println("Quadratic time step");
        }
    }
}
```

Since both loops run `n` times, the total number of steps is \( n \times n = n^2 \).

### 6. **O(2^n) - Exponential Time**
Exponential time complexity doubles the amount of work with each increase in input size. Recursion is often used to achieve exponential complexity, but it can also be done with loops, though it’s less common.

```java
void exponentialTime(int n) {
    int limit = (int) Math.pow(2, n);
    for (int i = 0; i < limit; i++) {
        System.out.println("Exponential time step");
    }
}
```

This loop runs \( 2^n \) times, where each increase in `n` doubles the number of iterations.

### 7. **O(n!) - Factorial Time**
Factorial time complexity involves permutations or combinations, and the number of steps grows factorially with the input size. This example generates all permutations of a list.

```java
void factorialTime(int n) {
    // Generating all permutations is an O(n!) operation
    permute("", "ABC"); // Example for n = 3 with string "ABC"
}

void permute(String prefix, String str) {
    int n = str.length();
    if (n == 0) System.out.println("Factorial time step: " + prefix);
    else {
        for (int i = 0; i < n; i++) {
            permute(prefix + str.charAt(i), str.substring(0, i) + str.substring(i + 1, n));
        }
    }
}
```

This recursion example generates all possible permutations of a string, with a time complexity of \( O(n!) \) due to the factorial growth in the number of permutations.

### Summary Table

| Complexity      | Example Code                        | Description                                            |
|-----------------|------------------------------------|--------------------------------------------------------|
| **O(1)**        | `constantTime`                     | Executes in constant time regardless of input size.    |
| **O(log n)**    | `logarithmicTime`                  | Halves the problem size each iteration.                |
| **O(n)**        | `linearTime`                       | Runs once per element.                                 |
| **O(n log n)**  | `logLinearTime`                    | Executes log n steps for each of n elements.           |
| **O(n^2)**      | `quadraticTime`                    | Runs n times for each of n elements.                   |
| **O(2^n)**      | `exponentialTime`                  | Doubles steps for each increase in input size.         |
| **O(n!)**       | `factorialTime` (permutations)     | Explores all permutations, factorial growth.           |

Each example illustrates how loop structure affects the runtime complexity.