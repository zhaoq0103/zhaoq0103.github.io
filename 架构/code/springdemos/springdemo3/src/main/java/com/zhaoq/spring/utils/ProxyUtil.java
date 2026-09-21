package com.zhaoq.spring.utils;

import com.zhaoq.spring.service.UserService;
import com.zhaoq.spring.service.UserServiceImpl;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import javax.jws.soap.SOAPBinding;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyUtil {
    public static UserService getProxyService(final UserService service){
        UserService proxy = (UserService) Proxy.newProxyInstance(service.getClass().getClassLoader(), service.getClass().getInterfaces(), new InvocationHandler() {
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println("开始记录日志。。");
                Object o = method.invoke(service, args);
//                Object o = method.invoke(proxy, args); //调用对象的方法
                System.out.println("记录日志结束。。");
                return o;
            }
        });
        return proxy;
    }

    public static UserService getGCLibProxyService(final UserService service){
        Enhancer ex = new Enhancer();
        ex.setSuperclass(service.getClass());
//        ex.setSuperclass(UserServiceImpl.class);
        ex.setCallback(new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                long start = System.currentTimeMillis();
                System.out.println("begin time:" + start);
                Object o2 = methodProxy.invokeSuper(o, objects);
                long end = System.currentTimeMillis();
                System.out.println("end time:" + end);
                long time = (end - start);
                System.out.println("run time:" + time + "豪秒");
                return o2;
            }
        });
        UserService us = (UserService)ex.create();

        return us;
    }

}
