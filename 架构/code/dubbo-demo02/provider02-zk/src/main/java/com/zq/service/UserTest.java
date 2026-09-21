package com.zq.service;

import com.zq.common.IDoSomething;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class UserTest {

    public static void main(String[] args) throws IOException {
        ApplicationContext ac = new ClassPathXmlApplicationContext("dubbo-provider-01.xml");
        ((ClassPathXmlApplicationContext) ac).start();

        System.in.read();
    }
}
