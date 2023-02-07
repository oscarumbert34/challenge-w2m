package com.w2m.superheroe.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Aspect
@Slf4j
public class LoggingAspect {

    @Pointcut("@annotation(Log)")
    public void logPointcut(){
        log.info("aspect");
    }

    @Around("logPointcut()")
    public Object processTime(ProceedingJoinPoint joinPoint) throws Throwable {

        long startTime=System.currentTimeMillis();

        Object methodProceed=joinPoint.proceed();

        long endTime=System.currentTimeMillis();
        var totalTime = endTime - startTime;
        log.info("Class "+ joinPoint.getSignature().toString() + "tarda " + totalTime + " ms");

        return methodProceed;

    }
}
