package com.zhaoq.spring.service;


public class UserServiceImpl implements UserService {
    private String name;
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

    @Override
    public void saveUser() {
        System.out.println("saveUser:" + name + " ,id:" + uid);
    }
}
