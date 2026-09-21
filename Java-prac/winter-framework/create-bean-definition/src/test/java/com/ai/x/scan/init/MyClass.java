package com.ai.x.scan.init;

import com.ai.x.annotation.Autowired;
import com.ai.x.annotation.Component;
import com.ai.x.scan.primary.DogBean;
import org.slf4j.LoggerFactory;

@Component
public class MyClass {
    public MyClass() {
        // 无参构造函数
    }

    @Autowired
    public void  setDog(DogBean dog) {
        // 无参构造函数
        LoggerFactory.getLogger(getClass()).atDebug().log("set Dog : {}",dog);
    }

    private MyClass(int value) {
        // 私有构造函数
    }

//    public MyClass(String name, int age) {
//        // 公共构造函数
//    }
}