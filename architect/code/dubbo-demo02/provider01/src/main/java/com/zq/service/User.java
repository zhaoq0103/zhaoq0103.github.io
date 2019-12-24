package com.zq.service;

import com.zq.common.IDoSomething;

public class User implements IDoSomething {
    @Override
    public void doSomething() {
        System.out.println("I'm Coming...");
    }
}
