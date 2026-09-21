package com.ai.x.push;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * WebSocket push based on VertX.
 * 把PushApplication放在com.ai.x.push包下面，以避免自动扫描到com.ai.x 包下的组件（如RedisService） -- ?
 */
@SpringBootApplication
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class) // 禁用数据库自动配置 (无DataSource, JdbcTemplate...)
public class Main {
    public static void main(String[] args) {
        System.setProperty("vertx.disableFileCPResolving", "true");
        System.setProperty("vertx.logger-delegate-factory-class-name", "io.vertx.core.logging.SLF4JLogDelegateFactory");
        SpringApplication app = new SpringApplication(Main.class);
        // 禁用Spring的Web:
        app.setWebApplicationType(WebApplicationType.NONE);
        app.run(args);
    }
}