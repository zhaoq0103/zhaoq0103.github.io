package com.zhaoq.spring.test;

import com.zhaoq.spring.configuration.SpringConfig;
import com.zhaoq.spring.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
//classes：纯注解方式时，读取配置类
@ContextConfiguration(classes = SpringConfig.class)
//locations：XML方式时，读取配置文件
//@ContextConfiguration(locations="classpath:applicationContext.xml")
public class UserServiceTest03 {

    @Resource
    private UserService userService;

//    @Before
//    public void init(){
//        ApplicationContext ac = new AnnotationConfigApplicationContext(SpringConfig.class);
//        this.userService =  ac.getBean(UserService.class);
//    }

    @Test
    public void test(){
        this.userService.saveUser();
    }
}
