package com.zhaoq.spring.test;

import com.zhaoq.spring.service.UserService;
import com.zhaoq.spring.service.UserServiceImpl;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class UserServiceTest {

    @Test
    public void test1(){

        ApplicationContext ac = new ClassPathXmlApplicationContext("appContext.xml");
        UserService us1 =  ac.getBean(UserServiceImpl.class);
        us1.saveUser();
        UserService us2 = (UserService) ac.getBean("userService");
        us2.saveUser();
    }
}
