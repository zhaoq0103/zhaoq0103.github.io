package com.zhaoq.spring.test;

import com.zhaoq.spring.configuration.SpringConfig;
import com.zhaoq.spring.service.UserService;
import com.zhaoq.spring.service.UserServiceImpl;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class UserServiceTest {

    @Test
    public void test1(){

        ApplicationContext ac = new  AnnotationConfigApplicationContext(SpringConfig.class);
        UserService us1 =  ac.getBean(UserService.class);
        us1.saveUser();

        UserService us2 = (UserService) ac.getBean("userService");
        us2.saveUser();
    }
}
