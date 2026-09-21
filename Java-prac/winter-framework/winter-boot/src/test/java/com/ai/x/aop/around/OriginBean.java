package com.ai.x.aop.around;


import com.ai.x.annotation.Around;
import com.ai.x.annotation.Component;
import com.ai.x.annotation.Value;

@Component
@Around("aroundInvocationHandler")
public class OriginBean {

    @Value("${customer.name}")
    public String name;

    @Polite
    public String hello() {
        return "Hello, " + name + ".";
    }

    public String morning() {
        return "Morning, " + name + ".";
    }
}
