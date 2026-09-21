package com.zhaoq.spring.advice;


import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component(value = "myAdvice")
public class MyAdvice {
    private static final String EXPRESS = "execution(void com.zhaoq.spring.service.UserServiceImpl.saveUser())";

    @Before(value = EXPRESS)
    public void logBefore(){
       System.out.println("log before..");
    }

    @After(value = EXPRESS)
    public void logAfter(){
        System.out.println("log after..");
    }
}
