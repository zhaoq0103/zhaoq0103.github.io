package com.ai.x.scan.proxy;


import com.ai.x.annotation.Autowired;
import com.ai.x.annotation.Component;

@Component
public class InjectProxyOnConstructorBean {

    public final OriginBean injected;

    public InjectProxyOnConstructorBean(@Autowired OriginBean injected) {
        this.injected = injected;
    }
}
