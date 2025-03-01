package com.example.bootcamp.aop;

import com.example.bootcamp.repository.ExceptionLogRepository;
import lombok.AllArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
@AllArgsConstructor
public class LoggingAspect {

  private final ExceptionLogRepository exceptionLogRepository;

  @Around("execution(* com.example.bootcamp.controller.*.*(..))") // Pointcut expression
  public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
    long startTime = System.currentTimeMillis();

    // Proceed with the method execution
    Object proceed = joinPoint.proceed();

    long timeTaken = System.currentTimeMillis() - startTime;
    System.out.println(joinPoint.getSignature() + " executed in " + timeTaken + " ms");

    return proceed;
  }

  @Before("execution(* com.example.bootcamp.service.UserService.*(..))") // Pointcut expression
  public void logBeforeMethod() {
    System.out.println("Before method execution");
  }

  @After("execution(* com.example.bootcamp.service.UserService.*(..))")
  public void logAfterMethod() {
    System.out.println("After method execution");
  }

  /*@AfterThrowing(pointcut = "execution(* com.example.bootcamp..*(..))", throwing = "exception")
  public void logException(JoinPoint joinPoint, Throwable exception) {
    exception.printStackTrace();
    ExceptionLog log = new ExceptionLog();
    log.setExceptionMessage(exception.getMessage());
    log.setStackTrace(Arrays.toString(exception.getStackTrace()));
    log.setMethodName(joinPoint.getSignature().getName());
    log.setClassName(joinPoint.getSignature().getDeclaringTypeName());
    log.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));

    exceptionLogRepository.save(log);
  }*/
}
