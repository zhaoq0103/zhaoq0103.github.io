package com.ai.x.scan.proxy;


import com.ai.x.annotation.Autowired;
import com.ai.x.annotation.Component;

@Component
public class InjectProxyOnPropertyBean {

    @Autowired
    public OriginBean injected;
}
