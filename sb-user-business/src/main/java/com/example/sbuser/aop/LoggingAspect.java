package com.example.sbuser.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

  @Around("execution(* com.example.sbuser.controller.*.*(..))") // Pointcut expression
  public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
    long startTime = System.currentTimeMillis();

    // Proceed with the method execution
    Object proceed = joinPoint.proceed();

    long timeTaken = System.currentTimeMillis() - startTime;
    System.out.println(joinPoint.getSignature() + " executed in " + timeTaken + " ms");

    return proceed;
  }

  @Before("execution(* com.example.sbuser.service.UserService.*(..))") // Pointcut expression
  public void logBeforeMethod() {
    System.out.println("Before method execution");
  }

  @After("execution(* com.example.sbuser.service.UserService.*(..))")
  public void logAfterMethod() {
    System.out.println("After method execution");
  }

  @AfterThrowing("execution(* com.example.sbuser.service.UserService.*(..))")
  public void logAfterThrowing() {
    System.out.println("Exception thrown by the method");
  }
}
