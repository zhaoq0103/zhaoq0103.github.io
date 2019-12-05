package com.zhaoq.spring.configuration;

import com.zhaoq.spring.service.UserService;
import com.zhaoq.spring.service.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
//需要扫描哪些包
@ComponentScan(basePackages ="com.zhaoq.spring.service")
public class SpringConfig {

//    @Bean
//    public UserService userService(){
//        return new UserServiceImpl();
//    }
}
