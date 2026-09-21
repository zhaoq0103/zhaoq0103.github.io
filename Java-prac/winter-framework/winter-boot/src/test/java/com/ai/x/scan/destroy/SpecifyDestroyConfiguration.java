package com.ai.x.scan.destroy;

import com.ai.x.annotation.Bean;
import com.ai.x.annotation.Component;
import com.ai.x.annotation.Configuration;
import com.ai.x.annotation.Value;
import jakarta.annotation.PostConstruct;

@Configuration
public class SpecifyDestroyConfiguration {

    @Bean(destroyMethod = "destroy")
    SpecifyDestroyBean createSpecifyDestroyBean(@Value("${app.title}") String appTitle) {
        return new SpecifyDestroyBean(appTitle);
    }
}
