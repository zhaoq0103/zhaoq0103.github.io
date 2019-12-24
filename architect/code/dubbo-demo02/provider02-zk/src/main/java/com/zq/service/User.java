package com.zq.service;

import com.zq.common.IDoSomething;

public class User implements IDoSomething {
    @Override
    public String doSomething() {
        System.out.println("I'm Coming...");
        return "Finished02..";
    }
}
