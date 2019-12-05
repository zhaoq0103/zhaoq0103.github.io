package com.zhaoq.spring.configuration;

import org.springframework.context.annotation.Configuration;

@Configuration
public class DaoConfig {
    public DaoConfig(){
        System.out.println("DaoConfig loaded");
    }
}
