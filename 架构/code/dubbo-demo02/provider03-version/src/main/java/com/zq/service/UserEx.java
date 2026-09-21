package com.zq.service;

import com.zq.common.IDoSomething;

public class UserEx implements IDoSomething {
    @Override
    public String doSomething() {
        System.out.println("Experts Coming...");
        return "Finished..ex";
    }
}
