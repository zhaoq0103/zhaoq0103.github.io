package com.ai.x.aop.after;


import com.ai.x.annotation.Bean;
import com.ai.x.annotation.ComponentScan;
import com.ai.x.annotation.Configuration;
import com.ai.x.aop.AroundProxyBeanPostProcessor;

@Configuration
@ComponentScan
public class AfterApplication {

    @Bean
    AroundProxyBeanPostProcessor createAroundProxyBeanPostProcessor() {
        return new AroundProxyBeanPostProcessor();
    }
}
