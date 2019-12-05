package com.zhaoq.spring.service;

import com.zhaoq.spring.configuration.DaoConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@Service(value = "userService")
@PropertySource("classpath:config.properties")
@Import(value = DaoConfig.class)
public class UserServiceImpl implements UserService {
    @Value("${name}")
    private String name;
    @Value("${uid}")
    private int uid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public void saveUser() {
        System.out.println("saveUser:" + name + " ,id:" + uid);
    }
}
