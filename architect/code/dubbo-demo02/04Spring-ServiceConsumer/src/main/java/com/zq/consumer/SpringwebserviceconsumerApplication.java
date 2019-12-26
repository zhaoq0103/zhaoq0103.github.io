package com.zq.consumer;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubboConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubboConfig
public class SpringwebserviceconsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringwebserviceconsumerApplication.class, args);
    }

}
