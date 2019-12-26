package com.zq.service;

import com.zq.common.IDoSomething;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class RunConsumer {
    public static void main(String[] args){
        ApplicationContext ac = new ClassPathXmlApplicationContext("dubbo-consumer-01.xml");
        IDoSomething service = (IDoSomething) ac.getBean("something");
        String ret = service.doSomething();
        System.out.println("returned:" + ret);
    }
}
