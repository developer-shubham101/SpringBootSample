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

  @Around("execution(* in.newdevpoint.bootcamp.controller.*.*(..))") // Pointcut expression
  public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
    long startTime = System.currentTimeMillis();

    // Proceed with the method execution
    Object proceed = joinPoint.proceed();

    long timeTaken = System.currentTimeMillis() - startTime;
    System.out.println(joinPoint.getSignature() + " executed in " + timeTaken + " ms");

    return proceed;
  }

  @Before("execution(* in.newdevpoint.bootcamp.service.UserService.*(..))") // Pointcut expression
  public void logBeforeMethod() {
    System.out.println("Before method execution");
  }

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
