package com.x.ai.ioc;

import com.x.ai.ioc.bean.User;
import com.x.ai.ioc.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class Main {

    @SuppressWarnings("resource")
    public static void main(String[] args) {
//        ApplicationContext context = new ClassPathXmlApplicationContext("application.xml");
        ApplicationContext context = new AnnotationConfigApplicationContext(Main.class);
        UserService userService = context.getBean(UserService.class);
        userService.register("bob@example.com", "password", "Bob");
        User user = userService.login("bob@example.com", "password");
        System.out.println(user.getName());
    }

}
