package com.example.urlshortner.aspect;

import com.example.urlshortner.dto.response.BaseResponse;
import com.example.urlshortner.exception.UrlShortnerException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collection;

@Aspect
@Component
@Slf4j
public class LogAspect {

    @Pointcut("within(com.example.urlshortner..*)")
    public void inScope() {
    }


    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void restController() {
    }

    @Pointcut("execution(public com.example.urlshortner.dto.BaseResponse *(..))")
    public void baseResponse() {
    }

    @Around(value = "inScope() && restController()")
    public Object loggingAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Signature signature = joinPoint.getSignature();
        log.info("Request {}.{} with args={}", signature.getDeclaringTypeName(), signature.getName(), joinPoint.getArgs());

        Object response = joinPoint.proceed();

        log.info("Response for {}.{} with response={}, Time Taken={} ms",
                signature.getDeclaringTypeName(), signature.getName(), response, System.currentTimeMillis() - startTime);

        return response;
    }

    @AfterReturning(value = "inScope() && restController() && baseResponse()",
            returning = "response")
    public void logAfterReturning(JoinPoint joinPoint, BaseResponse<?> response) throws Throwable {
        Object body = response.getBody();
        if (body instanceof Collection<?>) {
            response.setSize(((Collection<?>) body).size());
        }
        response.setLocalDateTime(LocalDateTime.now());
    }

    @AfterThrowing(value = "inScope() && restController()", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, UrlShortnerException ex) {
        log.error("Exception in {}.{} with message= {}",
                joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(), ex.getMessage());
    }
}