package com.zhaoq.spring.test;

import com.zhaoq.spring.configuration.SpringConfig;
import com.zhaoq.spring.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class UserServiceTest02 {
    private UserService userService;

    @Before
    public void init(){
        ApplicationContext ac = new AnnotationConfigApplicationContext(SpringConfig.class);
        this.userService =  ac.getBean(UserService.class);
    }

    @Test
    public void test(){
        this.userService.saveUser();
    }
}
