package in.newdevpoint.bootcamp.controller;

import in.newdevpoint.bootcamp.data.SampleData;
import in.newdevpoint.bootcamp.usecase.OrderService;
import in.newdevpoint.bootcamp.usecase.SystemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system")
// @PreAuthorize(RoleConstants.ADMIN_CRUD)
public class SystemController {

  private static final Logger logger = LoggerFactory.getLogger(SystemController.class);

  private final Environment environment;

  @Autowired SystemService systemService;
  @Autowired private OrderService orderService;

  public SystemController(Environment environment) {
    this.environment = environment;
  }

  @GetMapping("/active-profile")
  public String getActiveProfile() {
    String googleMapKey = environment.getProperty("google.map.key");
    String apiKey = environment.getProperty("API_KEY");

    return "Active profile: "
        + String.join(", ", environment.getActiveProfiles())
        + "\n"
        + googleMapKey
        + "\n"
        + apiKey;
  }

  @GetMapping("/external-rest-api")
  public Object fetchExternalApi() {
    try {
      return systemService.fetchExternalApi();
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Data Not Found");
    }
  }

  @GetMapping("/place-order")
  public Object placeOrder() {
    String orderInfo = orderService.placeOrder();

    if (orderInfo == null) {
      // If order failed then initiateRefund and let say we don't
      // need to wait for refund operation response because it's long process and
      // doesn't require anywhere it's response then we can use @Async
      orderService.initiateRefund();

    } else {
      // After order place send email/notification to user
      // We don't want to wait for response from email API
      // so we will use @Async method for that
      orderService.sendOrderConfirmationEmail(SampleData.emailList, orderInfo);
    }

    return new ResponseEntity<>(orderInfo, HttpStatus.OK);
  }

  @GetMapping("/read-resource-file")
  public Object readResourceFile() {
    String orderInfo = systemService.readFile();

    return new ResponseEntity<>(orderInfo, HttpStatus.OK);
  }

  @GetMapping("/process")
  public String processRequest() {
    try {
      // Simulating a long-running task
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      logger.error("Exception occurred while processing /error endpoint", e);
      e.printStackTrace();
    }
    return "Processed by " + Thread.currentThread().getName();
  }
}
