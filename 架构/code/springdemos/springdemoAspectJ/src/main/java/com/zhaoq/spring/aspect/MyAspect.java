package com.zhaoq.spring.aspect;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
//<!-- 这里必须要注册bean,才能使用切面功能, 要么用注解@Component注册， 要么用XML注册 -->
@Component(value = "myaspe")
public class MyAspect {
    private static final String EXPRESS2 = "execution(* com.zhaoq.spring.aspect.UserServiceImpl.add(..))";

    @Before(value = EXPRESS2)
    public void before(){
        System.out.println("前置增强....");
    }

    @Around(value = "execution(* com.zhaoq.spring.aspect.UserServiceImpl.find(..))")
    public void around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        System.out.println("环绕前增强....");
        Object obj = proceedingJoinPoint.proceed();
        System.out.println("环绕后增强....");
    }

}
