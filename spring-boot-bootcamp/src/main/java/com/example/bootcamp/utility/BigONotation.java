package com.example.bootcamp.utility;

import org.springframework.stereotype.Component;

@Component
public class BigONotation {

  public void constantTime(int[] array) {
    System.out.println(array[0]); // Accessing an element by index is O(1)
  }

  public void logarithmicTime(int n) {
    for (int i = 1; i < n; i *= 2) { // i doubles each iteration
      System.out.println("Logarithmic time step: " + i);
    }
  }

  public void linearTime(int[] array) {
    for (int j : array) {
      System.out.println("Linear time step for element " + j);
    }
  }

  public void logLinearTime(int n) {
    for (int i = 0; i < n; i++) { // Runs n times
      for (int j = 1; j < n; j *= 2) { // Inner loop runs log n times
        System.out.println("Log-linear time step");
      }
    }
  }

  public void quadraticTime(int n) {
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        System.out.println("Quadratic time step");
      }
    }
  }

  public void exponentialTime(int n) {
    int limit = (int) Math.pow(2, n);
    for (int i = 0; i < limit; i++) {
      System.out.println("Exponential time step");
    }
  }

  public void factorialTime(int n) {
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
}
