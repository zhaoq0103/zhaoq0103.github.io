package com.x.ai.ioc;

import com.x.ai.ioc.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;

import java.time.ZoneId;

@Configuration
@ComponentScan
@EnableAspectJAutoProxy
public class AppConfig {

    @SuppressWarnings("resource")
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        UserService userService = context.getBean(UserService.class);
        userService.login("bob@example.com", "password");
    }

    @Bean
//    @Profile("!test")
//    @Profile("pro")
    @Profile("uat")
//    环境变量的配置方式： smtp=true;spring.profiles.active=uat
//    这个配置影响程序的运行
    ZoneId createZoneId() {
        return ZoneId.systemDefault();
    }

    @Bean
    @Profile("test")
    ZoneId createZoneIdForTest() {
        return ZoneId.of("America/New_York");
    }
}
