package com.ai.x.controller;

import com.ai.x.bean.User;
import com.ai.x.framework.GetMapping;
import com.ai.x.framework.ModelAndView;
import jakarta.servlet.http.HttpSession;

public class IndexController {
    @GetMapping("/")
    public ModelAndView index(HttpSession session) {
        User user = (User) session.getAttribute("user");
        return new ModelAndView("/index.html", "user", user);
    }

    @GetMapping("/hello")
    public ModelAndView hello(String name) {
        if (name == null) {
            name = "World";
        }
        return new ModelAndView("/hello.html", "name", name);
    }

}
