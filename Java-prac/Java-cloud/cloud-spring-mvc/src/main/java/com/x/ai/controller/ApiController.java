package com.x.ai.controller;

import com.x.ai.entity.User;
import com.x.ai.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    UserService userService;

    @GetMapping("/users")
    public List<User> users() {
        return userService.getAllUsers(0);
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable("id") long id) {
        return userService.getUserById(id);
    }


    @PostMapping("/signin")
    public Map<String, Object> signin(@RequestBody SignInRequest signinRequest) {
        try {
            User user = userService.signin(signinRequest.email, signinRequest.password);
            Map<String, Object> map = new HashMap<>();
            map.put("user", user);
            return map;
        } catch (Exception e) {
            Map<String, Object> map = new HashMap<>();
            map.put("error", "SIGNIN_FAILED");
            map.put("message", e.getMessage());
            return map;
        }
    }


    //用 static 调用时才能反射到这个类
    private static class SignInRequest {
        public String email;
        public String password;
    }
}
