package com.ai.x.aop.after;

import com.ai.x.annotation.Component;
import com.ai.x.aop.AfterInvocationHandlerAdapter;

import java.lang.reflect.Method;


@Component
public class PoliteInvocationHandler extends AfterInvocationHandlerAdapter {

    @Override
    public Object after(Object returnValue, Object proxy, Method method, Object[] args) {
        if (returnValue instanceof String s) {
            if (s.endsWith(".")) {
                return s.substring(0, s.length() - 1) + "!";
            }
        }
        return returnValue;
    }
}
