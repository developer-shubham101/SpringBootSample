package in.newdevpoint.bootcamp.usecase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Random;

@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    /**
     * Asynchronously sends order confirmation emails to a list of recipients.
     *
     * Each email address in the provided list is processed in a separate thread, simulating a delay for sending.
     *
     * @param emailList list of recipient email addresses
     * @param orderDetails details of the order to include in the confirmation
     */
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

    /**
     * Initiates a refund process asynchronously, simulating a delay to represent processing time.
     *
     * This method runs in a separate thread and prints the current thread name and a confirmation message upon completion.
     */
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

    /**
     * Places an order synchronously and returns a generated order number.
     *
     * Simulates order processing by introducing a brief delay before generating and returning a unique order number string.
     *
     * @return the generated order number in the format "#OR-XXXX"
     */
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
