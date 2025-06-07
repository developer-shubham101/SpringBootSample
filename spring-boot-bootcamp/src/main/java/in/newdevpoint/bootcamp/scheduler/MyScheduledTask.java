package in.newdevpoint.bootcamp.scheduler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * This class demonstrates different ways to schedule tasks in Spring Boot using @Scheduled
 * annotation. It contains examples of fixed rate, fixed delay, and cron-based scheduling.
 */
@Slf4j // Lombok annotation to automatically create a logger instance
@Component // Marks this class as a Spring component, making it eligible for auto-detection
public class MyScheduledTask {

  /**
   * This method demonstrates fixed rate scheduling. The task will execute every 5 seconds
   * regardless of the previous task's completion time. If a task takes longer than 5 seconds, the
   * next task will start immediately after the previous one finishes.
   */
  @Scheduled(fixedRate = 5000) // 5000 milliseconds = 5 seconds
  public void executeTaskAtFixedRate() {
    log.info(
        "Task executed at fixed rate - "
            + System.currentTimeMillis() // Current time in milliseconds since epoch
            + " - "
            + LocalDateTime.now()
                .format(
                    DateTimeFormatter.ofPattern(
                        "dd.MMM.yyyy hh:mm:ss"))); // Formatted current date and time
  }

  /**
   * This method demonstrates fixed delay scheduling. The task will execute 3 seconds after the
   * previous task completes. Unlike fixed rate, this ensures a delay between task completions.
   */
  @Scheduled(fixedDelay = 3000) // 3000 milliseconds = 3 seconds
  public void executeTaskWithFixedDelay() {
    log.info(
        "Task executed with fixed delay - "
            + System.currentTimeMillis()
            + " - "
            + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MMM.yyyy hh:mm:ss")));
  }

  /**
   * This method demonstrates cron-based scheduling. Currently set to run every minute (0 * * * *
   * *). Other commented cron expressions show different scheduling patterns: - "0 30 23 * * ?" -
   * Runs at 11:30 PM every day - "0 0 * * * *" - Runs at the start of every hour - "0 0 9 * * ?" -
   * Runs at 9 AM every day
   *
   * <p>Cron expression format: "second minute hour day-of-month month day-of-week"
   */
  @Scheduled(cron = "0 * * * * *") // every minute
  //    @Scheduled(cron = "0 30 23 * * ?", zone = "Asia/Kolkata") // 11:30 pm
  //    @Scheduled(cron = "0 0 * * * *") // Every hr.
  //    @Scheduled(cron = "0 0 9 * * ?") // Task to run at specific times using a cron expression
  public void executeTaskWithCronExpression() {
    log.info(
        "Task executed with cron expression at 9 AM - "
            + System.currentTimeMillis()
            + " - "
            + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MMM.yyyy hh:mm:ss")));
  }
}
