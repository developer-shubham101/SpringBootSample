package com.example.sbuser.aop;

import com.example.sbuser.entity.ExceptionLog;
import com.example.sbuser.repository.ExceptionLogRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@AllArgsConstructor
public class ExceptionLoggingAspect {

  private final ExceptionLogRepository exceptionLogRepository;

  @AfterThrowing(pointcut = "execution(* com.example.sbuser..*(..))", throwing = "exception")
  public void logException(JoinPoint joinPoint, Throwable exception) {
    exception.printStackTrace();
    ExceptionLog log = new ExceptionLog();
    log.setExceptionMessage(exception.getMessage());
    log.setStackTrace(Arrays.toString(exception.getStackTrace()));
    log.setMethodName(joinPoint.getSignature().getName());
    log.setClassName(joinPoint.getSignature().getDeclaringTypeName());
    log.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));

    exceptionLogRepository.save(log);
  }
}
