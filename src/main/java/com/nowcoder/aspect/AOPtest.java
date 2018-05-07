package com.nowcoder.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

@Aspect
@Component
public class AOPtest {
    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Before("execution(* com.nowcoder.aspect.*.A(..))")
    public void beforeMethod(JoinPoint joinPoint) {

        logger.info("AOP test A方法执行之前");

    }

    @After("execution(* com.nowcoder.aspect.*.A(..))")
    public void afterMethod() {
        logger.info("AOP test A方法执行之后");
    }


}
