package com.ai.x.aop.before;

import com.ai.x.annotation.Around;
import com.ai.x.annotation.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@Component
@Around("logInvocationHandler")
public class BusinessBean {

    final Logger logger = LoggerFactory.getLogger(getClass());

    public String hello(String name) {
        logger.info("Hello, {}.", name);
        return "Hello, " + name + ".";
    }

    public String morning(String name) {
        logger.info("Morning, {}.", name);
        return "Morning, " + name + ".";
    }
}
