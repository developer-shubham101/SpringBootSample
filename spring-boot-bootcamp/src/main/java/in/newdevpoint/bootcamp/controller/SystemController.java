package in.newdevpoint.bootcamp.controller;

import in.newdevpoint.bootcamp.data.SampleData;
import in.newdevpoint.bootcamp.usecase.OrderService;
import in.newdevpoint.bootcamp.usecase.SystemService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system")
@RequiredArgsConstructor
public class SystemController {

    private static final Logger logger = LoggerFactory.getLogger(SystemController.class);

    private final Environment environment;
    private final SystemService systemService;
    private final OrderService orderService;

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
            logger.error("Error fetching external API", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Data Not Found");
        }
    }

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
