package main.QuizCraft.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);

    @Around("execution(* main.QuizCraft.service..*(..)) || execution(* main.QuizCraft.repository..*(..)) || execution(* main.QuizCraft.controller..*(..))")
    public Object logAllMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        return logMethodExecution(joinPoint);
    }

    private Object logMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        LOGGER.info("Executing method: {}.{} with arguments: {}", className, methodName, args);

        long startTime = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - startTime;
            LOGGER.info("Method executed: {}.{} with result: {} in {} ms", className, methodName, result, executionTime);
            return result;
        } catch (Throwable throwable) {
            long executionTime = System.currentTimeMillis() - startTime;
            LOGGER.error("Exception in method: {}.{} with message: {} after {} ms", className, methodName, throwable.getMessage(), executionTime);
            throw throwable;
        }
    }
}