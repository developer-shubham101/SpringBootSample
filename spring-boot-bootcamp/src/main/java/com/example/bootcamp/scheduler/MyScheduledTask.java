package com.example.bootcamp.scheduler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MyScheduledTask {

  // Task to run at a fixed interval of 5 seconds
  @Scheduled(fixedRate = 5000)
  public void executeTaskAtFixedRate() {
    log.info(
        "Task executed at fixed rate - "
            + System.currentTimeMillis()
            + " - "
            + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MMM.yyyy hh:mm:ss")));
  }

  // Task to run with a fixed delay of 3 seconds after the previous task finishes
  @Scheduled(fixedDelay = 3000)
  public void executeTaskWithFixedDelay() {
    log.info(
        "Task executed with fixed delay - "
            + System.currentTimeMillis()
            + " - "
            + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MMM.yyyy hh:mm:ss")));
  }

  //    @Scheduled(cron = "0 30 23 * * ?", zone = "Asia/Kolkata") // 11:30 pm
  @Scheduled(cron = "0 * * * * *") // every minute
  //    @Scheduled(cron = "0 0 * * * *") // Every hr.
  //    @Scheduled(cron = "0 0 9 * * ?") // Task to run at specific times using a cron expression
  // (Every day at 9 AM)
  public void executeTaskWithCronExpression() {
    log.info(
        "Task executed with cron expression at 9 AM - "
            + System.currentTimeMillis()
            + " - "
            + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MMM.yyyy hh:mm:ss")));
  }
}
