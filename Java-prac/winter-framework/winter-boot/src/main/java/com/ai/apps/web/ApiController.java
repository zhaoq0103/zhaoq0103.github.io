package com.ai.apps.web;

import java.util.List;
import java.util.Map;

import com.ai.apps.User;
import com.ai.apps.service.UserService;
import com.ai.x.annotation.Autowired;
import com.ai.x.annotation.GetMapping;
import com.ai.x.annotation.PathVariable;
import com.ai.x.annotation.RestController;
import com.ai.x.exception.DataAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@RestController
public class ApiController {

    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    UserService userService;

    @GetMapping("/api/user/{email}")
    Map<String, Boolean> userExist(@PathVariable("email") String email) {
        if (!email.contains("@")) {
            throw new IllegalArgumentException("Invalid email");
        }
        try {
            userService.getUser(email);
            return Map.of("result", Boolean.TRUE);
        } catch (DataAccessException e) {
            return Map.of("result", Boolean.FALSE);
        }
    }

    @GetMapping("/api/users")
    List<User> users() {
        return userService.getUsers();
    }
}
