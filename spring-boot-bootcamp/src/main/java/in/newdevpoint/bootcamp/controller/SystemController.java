package in.newdevpoint.bootcamp.controller;

import in.newdevpoint.bootcamp.data.SampleData;
import in.newdevpoint.bootcamp.service.OrderService;
import in.newdevpoint.bootcamp.service.SystemService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system")
@RequiredArgsConstructor
public class SystemController {

  private static final Logger logger = LoggerFactory.getLogger(SystemController.class);

  private final Environment environment;
  private final SystemService systemService;
  private final OrderService orderService;

  /**
   * Returns the active Spring profiles and selected environment properties as a formatted string.
   *
   * <p>The response includes the active profiles, the value of "google.map.key", and the value of
   * "API_KEY", each on a new line.
   *
   * @return a string listing active profiles and specified environment properties
   */
  @GetMapping("/active-profile")
  public String getActiveProfile() {
    String googleMapKey = environment.getProperty("google.map.key");
    String apiKey = environment.getProperty("API_KEY");

    logger.trace("TRACE log example");
    logger.debug("DEBUG log example");
    logger.info("INFO log example");
    logger.warn("WARN log example");
    logger.error("ERROR log example");

    try {
      int x = 1 / 0; // Cause an exception
    } catch (Exception e) {
      logger.error("Caught an exception", e);
    }

    return "Active profile: "
        + String.join(", ", environment.getActiveProfiles())
        + "\n"
        + googleMapKey
        + "\n"
        + apiKey;
  }

  /**
   * Creates a log entry by saving data from the path, query parameters, and request body.
   *
   * @param pathVariables the path variables (if any)
   * @param queryParams the query parameters (if any)
   * @param requestBody the request body (if any)
   * @return a success message with HTTP 200 status
   */
  @PostMapping("/create-log")
  public ResponseEntity<String> createLog(
      @RequestParam(required = false) Map<String, String> queryParams,
      @RequestBody(required = false) Map<String, Object> requestBody) {
    logger.debug("Path variables: {}", "Captured by wildcard path");
    logger.debug("Query parameters: {}", queryParams);
    logger.debug("Request body: {}", requestBody);

    // Here you can add logic to save the data to a database or file if needed

    return ResponseEntity.ok("Log created successfully");
  }

  /**
   * Fetches data from an external API and returns the result.
   *
   * @return the data retrieved from the external API, or a 404 response with "Data Not Found" if an
   *     error occurs
   */
  @GetMapping("/external-rest-api")
  public Object fetchExternalApi() {
    try {
      return systemService.fetchExternalApi();
    } catch (Exception e) {
      logger.error("Error fetching external API", e);
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Data Not Found");
    }
  }

  /**
   * Places an order and handles post-order actions such as refunds or confirmation emails.
   *
   * <p>If the order is placed successfully, sends a confirmation email and returns the order
   * information with HTTP 200 status. If the order fails (order information is null), initiates a
   * refund. Returns HTTP 500 with an error message if an exception occurs during processing.
   *
   * @return ResponseEntity containing order information on success, or an error message on failure
   */
  @GetMapping("/place-order")
  public Object placeOrder() {
    try {
      String orderInfo = orderService.placeOrder();

      if (orderInfo == null) {
        orderService.initiateRefund();
      } else {
        orderService.sendOrderConfirmationEmail(SampleData.emailList, orderInfo);
      }

      return new ResponseEntity<>(orderInfo, HttpStatus.OK);
    } catch (Exception e) {
      logger.error("Error placing order", e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Order processing failed");
    }
  }

  /**
   * Reads a resource file and returns its content.
   *
   * @return the content of the resource file with HTTP 200 status, or an error message with HTTP
   *     500 status if reading fails
   */
  @GetMapping("/read-resource-file")
  public Object readResourceFile() {
    try {
      String orderInfo = systemService.readFile();
      return new ResponseEntity<>(orderInfo, HttpStatus.OK);
    } catch (Exception e) {
      logger.error("Error reading resource file", e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Failed to read resource file");
    }
  }

  /**
   * Simulates a processing delay and returns the name of the thread that handled the request.
   *
   * @return a message indicating which thread processed the request
   */
  @GetMapping("/process")
  public String processRequest() {
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      logger.error("Error during process simulation", e);
      Thread.currentThread().interrupt();
    }
    return "Processed by " + Thread.currentThread().getName();
  }
}
