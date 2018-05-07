package com.nowcoder.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AOPtestB {
    private static final Logger logger = LoggerFactory.getLogger(AOPtestB.class);
    @Before("execution(* com.nowcoder.aspect.*.B(..))")
    public void beforeMethod(JoinPoint joinPoint) {

        logger.info("AOP test B方法执行之前");

    }
    @After("execution(* com.nowcoder.aspect.*.B(..))")
    public void afterMethod() {
        logger.info("AOP test B方法执行之后");
    }
}
