package in.newdevpoint.bootcamp.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class BigONotation {

  private static final Logger logger = LoggerFactory.getLogger(BigONotation.class);

  public void constantTime(int[] array) {
    logger.info(String.valueOf(array[0])); // Accessing an element by index is O(1)
  }

  public void logarithmicTime(int n) {
    for (int i = 1; i < n; i *= 2) { // i doubles each iteration
      logger.info("Logarithmic time step: {}", i);
    }
  }

  public void linearTime(int[] array) {
    for (int j : array) {
      logger.info("Linear time step for element {}", j);
    }
  }

  public void logLinearTime(int n) {
    for (int i = 0; i < n; i++) { // Runs n times
      for (int j = 1; j < n; j *= 2) { // Inner loop runs log n times
        logger.info("Log-linear time step");
      }
    }
  }

  public void quadraticTime(int n) {
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        logger.info("Quadratic time step");
      }
    }
  }

  public void exponentialTime(int n) {
    int limit = (int) Math.pow(2, n);
    for (int i = 0; i < limit; i++) {
      logger.info("Exponential time step");
    }
  }

  public void factorialTime(int n) {
    // Generating all permutations is an O(n!) operation
    permute("", "ABC"); // Example for n = 3 with string "ABC"
  }

  void permute(String prefix, String str) {
    int n = str.length();
    if (n == 0) logger.info("Factorial time step: " + prefix);
    else {
      for (int i = 0; i < n; i++) {
        permute(prefix + str.charAt(i), str.substring(0, i) + str.substring(i + 1, n));
      }
    }
  }
}
