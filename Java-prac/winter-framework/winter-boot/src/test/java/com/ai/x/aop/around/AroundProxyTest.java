package com.ai.x.aop.around;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Properties;

import com.ai.x.context.AnnotationConfigApplicationContext;
import com.ai.x.io.PropertyResolver;
import org.junit.jupiter.api.Test;


public class AroundProxyTest {

    @Test
    public void testAroundProxy() {
        try (var ctx = new AnnotationConfigApplicationContext(AroundApplication.class, createPropertyResolver())) {
            OriginBean proxy = ctx.getBean(OriginBean.class);
            // OriginBean$ByteBuddy$8NoD1FcQ
            System.out.println(proxy.getClass().getName());

            // proxy class, not origin class:
            assertNotSame(OriginBean.class, proxy.getClass());
            // proxy.name not injected:
            assertNull(proxy.name);

            assertEquals("Hello, Bob!", proxy.hello());
            assertEquals("Morning, Bob.", proxy.morning());

            // test injected proxy:
            // OtherBean 构造时注入的是 OriginBean 的代理，依赖是先于普通类生成
            OtherBean other = ctx.getBean(OtherBean.class);
            assertSame(proxy, other.origin);
            assertEquals("Hello, Bob!", other.origin.hello());
        }
    }

    PropertyResolver createPropertyResolver() {
        var ps = new Properties();
        ps.put("customer.name", "Bob");
        var pr = new PropertyResolver(ps);
        return pr;
    }
}
