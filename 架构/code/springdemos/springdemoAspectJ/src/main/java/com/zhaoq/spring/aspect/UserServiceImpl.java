package com.zhaoq.spring.aspect;

import org.springframework.stereotype.Controller;

@Controller
public class UserServiceImpl  implements UserService{
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

    public void add(){

        System.out.println("add");
    }

    public void delete(){

        System.out.println("delete");
    }


    public void find(){

        System.out.println("find");
    }


    public void update(){
        System.out.println("update");
    }
}
