package com.ai.x.aop.around;


import com.ai.x.annotation.Autowired;
import com.ai.x.annotation.Component;
import com.ai.x.annotation.Order;

@Order(0)
@Component
public class OtherBean {

    public OriginBean origin;

    public OtherBean(@Autowired OriginBean origin) {
        this.origin = origin;
    }
}
