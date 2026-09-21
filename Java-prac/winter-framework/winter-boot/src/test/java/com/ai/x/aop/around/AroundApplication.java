package com.ai.x.aop.around;


import com.ai.x.annotation.Bean;
import com.ai.x.annotation.ComponentScan;
import com.ai.x.annotation.Configuration;
import com.ai.x.aop.AroundProxyBeanPostProcessor;

@Configuration
@ComponentScan
public class AroundApplication {

    @Bean
    AroundProxyBeanPostProcessor createAroundProxyBeanPostProcessor() {
        return new AroundProxyBeanPostProcessor();
    }
}
