package com.ai.x.aop.after;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Properties;

import com.ai.x.context.AnnotationConfigApplicationContext;
import com.ai.x.io.PropertyResolver;
import org.junit.jupiter.api.Test;


public class AfterProxyTest {

    @Test
    public void testAfterProxy() {
        try (var ctx = new AnnotationConfigApplicationContext(AfterApplication.class, createPropertyResolver())) {
            GreetingBean proxy = ctx.getBean(GreetingBean.class);
            // should change return value:
            assertEquals("Hello, Bob!", proxy.hello("Bob"));
            assertEquals("Morning, Alice!", proxy.morning("Alice"));
        }
    }

    PropertyResolver createPropertyResolver() {
        var ps = new Properties();
        var pr = new PropertyResolver(ps);
        return pr;
    }
}
