package in.newdevpoint.bootcamp.usecase;

import java.util.ArrayList;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

  private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

  @Async // This makes the method execute in a separate thread
  public void sendOrderConfirmationEmail(ArrayList<String> emailList, String orderDetails) {
    // Simulate a long-running email sending process

    emailList.forEach(
        email -> {
          try {
            System.out.println(Thread.currentThread().getName());
            Thread.sleep(1000);
            System.out.println("Email sent to " + email);
          } catch (InterruptedException e) {
            logger.error("Exception occurred while sending order confirmation email", e);
            e.printStackTrace();
          }
        });
  }

  @Async // This makes the method execute in a separate thread
  public void initiateRefund() {
    try {
      System.out.println(Thread.currentThread().getName());

      Thread.sleep(2000);
      System.out.println("Refund Initiated");
    } catch (InterruptedException e) {
      logger.error("Exception occurred while initiating refund", e);
      e.printStackTrace();
    }
  }

  public String placeOrder() {

    try {
      Thread.sleep(300);
      logger.info("Order placed.");
    } catch (InterruptedException e) {
      logger.error("Exception occurred while processing /error endpoint", e);
      e.printStackTrace();
    }

    // initialize a Random object somewhere; you should only need one
    Random random = new Random();

    // generate a random integer from 0 to 899, then add 100
    int orderNo = random.nextInt(9000) + 1000;
    return "#OR-" + orderNo;
  }
}
