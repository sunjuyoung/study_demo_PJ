package com.demo.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class PerAspect {

    //@Around("execution(* com.demo..*.EventService.*(..))")
    @Around("@annotation(PerLogging)")
    //@Around("bean(simpleEventService)")
    public Object log(ProceedingJoinPoint pjp) throws Throwable{
        long begin = System.currentTimeMillis();
        Object retVal = pjp.proceed();
        System.out.println(System.currentTimeMillis() - begin);
        return retVal;
    }


    @Before("bean(simpleEventService)")
    public void logBefore(){
        System.out.println("hello");
    }
}
