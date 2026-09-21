package com.x.ai.ioc.service;

import com.x.ai.ioc.aspect.MetricTime;
import com.x.ai.ioc.mail.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class UserService {

    @Autowired(required = false)
    private MailService mailService;

    @Autowired
    private ZoneId zoneId;

// java8   Arrays.asList
// java9   List.of
    private List<User> users = new ArrayList<>(Arrays.asList( // users:
            new User(1, "bob@example.com", "password", "Bob"), // bob
            new User(2, "alice@example.com", "password", "Alice"), // alice
            new User(3, "tom@example.com", "password", "Tom"))); // tom

    // 监控register()方法性能:
    @MetricTime("login")
    public User login(String email, String password) {
        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email) && user.getPassword().equals(password)) {
                if (mailService != null) {
                    mailService.sendMail(user.getEmail(), "Login successfully",
                            "Hi, you have logged in at " + ZonedDateTime.now(this.zoneId).format(DateTimeFormatter.ISO_ZONED_DATE_TIME));
                } else {
                    System.out.println("skip send email.");
                }
                return user;
            }
        }
        throw new RuntimeException("login failed.");
    }
}
