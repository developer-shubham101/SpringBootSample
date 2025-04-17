package in.newdevpoint.bootcamp.aop;

import in.newdevpoint.bootcamp.repository.ExceptionLogRepository;
import lombok.AllArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
@AllArgsConstructor
public class LoggingAspect {

  private final ExceptionLogRepository exceptionLogRepository;

  /**
   * Measures and logs the execution time of controller methods in the specified package.
   *
   * @param joinPoint the join point representing the intercepted method
   * @return the result of the intercepted method execution
   * @throws Throwable if the intercepted method throws any exception
   */
  @Around("execution(* in.newdevpoint.bootcamp.controller.*.*(..))") // Pointcut expression
  public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
    long startTime = System.currentTimeMillis();

    // Proceed with the method execution
    Object proceed = joinPoint.proceed();

    long timeTaken = System.currentTimeMillis() - startTime;
    System.out.println(joinPoint.getSignature() + " executed in " + timeTaken + " ms");

    return proceed;
  }

  /****
   * Logs a message before any method execution in the UserService class.
   */
  @Before("execution(* in.newdevpoint.bootcamp.service.UserService.*(..))") // Pointcut expression
  public void logBeforeMethod() {
    System.out.println("Before method execution");
  }

  /**
   * Logs a message after the execution of any method in the UserService class.
   */
  @After("execution(* in.newdevpoint.bootcamp.service.UserService.*(..))")
  public void logAfterMethod() {
    System.out.println("After method execution");
  }

  /*@AfterThrowing(pointcut = "execution(* in.newdevpoint.bootcamp..*(..))", throwing = "exception")
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
