package com.ai.x.scan.destroy;

import com.ai.x.annotation.Component;
import com.ai.x.annotation.Value;
import jakarta.annotation.PostConstruct;

import jakarta.annotation.PreDestroy;

@Component
public class AnnotationDestroyBean {

    @Value("${app.title}")
    public String appTitle;

    @PreDestroy
    void destroy() {
        this.appTitle = null;
    }
}
