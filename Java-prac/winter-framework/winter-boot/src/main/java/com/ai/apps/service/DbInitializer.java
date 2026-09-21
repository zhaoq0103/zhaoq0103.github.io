package com.ai.apps.service;

import com.ai.x.annotation.Autowired;
import com.ai.x.annotation.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import jakarta.annotation.PostConstruct;

@Component
public class DbInitializer {

    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    UserService userService;

    @PostConstruct
    void init() {
        logger.info("init database...");
        userService.initDb();
    }
}
