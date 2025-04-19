package in.newdevpoint.bootcamp.usecase;

import java.util.ArrayList;
import java.util.Random;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

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
      e.printStackTrace();
    }
  }

  public String placeOrder() {

    try {
      Thread.sleep(300);
      System.out.println("Order placed.");
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    // initialize a Random object somewhere; you should only need one
    Random random = new Random();

    // generate a random integer from 0 to 899, then add 100
    int orderNo = random.nextInt(9000) + 1000;
    return "#OR-" + orderNo;
  }
}
