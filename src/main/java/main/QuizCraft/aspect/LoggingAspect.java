package main.QuizCraft.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

//@Aspect
//@Component
//public class LoggingAspect {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);
//
//    @Around("execution(* main.QuizCraft.service..*(..))")
//    public Object logServiceMethods(ProceedingJoinPoint joinPoint) throws Throwable {
//        return logMethodExecution(joinPoint, "Service");
//    }
//
//    @Around("execution(* main.QuizCraft.controller..*(..))")
//    public Object logControllerMethods(ProceedingJoinPoint joinPoint) throws Throwable {
//        return logMethodExecution(joinPoint, "Controller");
//    }
//
//    @Around("execution(* main.QuizCraft.repository..*(..))")
//    public Object logRepositoryMethods(ProceedingJoinPoint joinPoint) throws Throwable {
//        return logMethodExecution(joinPoint, "Repository");
//    }
//
//    private Object logMethodExecution(ProceedingJoinPoint joinPoint, String layer) throws Throwable {
//        String className = joinPoint.getSignature().getDeclaringTypeName();
//        String methodName = joinPoint.getSignature().getName();
//        Object[] args = joinPoint.getArgs();
//        LOGGER.info("[{}] Executing method: {}.{} with arguments: {}", layer, className, methodName, args);
//
//        long startTime = System.currentTimeMillis();
//        try {
//            Object result = joinPoint.proceed();
//            long executionTime = System.currentTimeMillis() - startTime;
//            LOGGER.info("[{}] Method executed: {}.{} with result: {} in {} ms", layer, className, methodName, result, executionTime);
//            return result;
//        } catch (Throwable throwable) {
//            long executionTime = System.currentTimeMillis() - startTime;
//            LOGGER.error("[{}] Exception in method: {}.{} with message: {} after {} ms", layer, className, methodName, throwable.getMessage(), executionTime);
//            throw throwable;
//        }
//    }
//}