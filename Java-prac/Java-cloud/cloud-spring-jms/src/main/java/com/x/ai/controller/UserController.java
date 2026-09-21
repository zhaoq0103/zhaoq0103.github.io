package com.x.ai.controller;

import com.x.ai.entity.MailMessage;
import com.x.ai.entity.User;
import com.x.ai.service.MessagingService;
import com.x.ai.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
//@RequestMapping("/user")
public class UserController {
    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    UserService userService;

    @Autowired
    MessagingService messagingService;
    public static final String KEY_USER = "__user__";

    @GetMapping("/")
    public ModelAndView index(HttpSession session) {
        User user = (User) session.getAttribute(KEY_USER);
        Map<String, Object> model = new HashMap<>();
        if (user != null) {
            model.put("user", model);
        }
        return new ModelAndView("/index.html", model);
    }

    @GetMapping("/register")
    public ModelAndView register() {
        return new ModelAndView("register.html");
    }

    @PostMapping("/register")
    public ModelAndView doRegister(@RequestParam("email") String email, @RequestParam("password") String password, @RequestParam("name") String name) {
        try {
            User user = userService.register(email, password, name);
            logger.info("user registered: {}", user.getEmail());
            messagingService.sendMailMessage(MailMessage.registration(user.getEmail(), user.getName()));
        } catch (Exception e) {
            Map<String, Object> map = new HashMap();
            map.put("email", email);
            map.put("error", "Register failed");

            return new ModelAndView("register.html", map);
        }
        return new ModelAndView("redirect:/signin");
    }


    @GetMapping("/signin")
    public ModelAndView signin(HttpSession session) {
        User user = (User) session.getAttribute(KEY_USER);
        if (user != null) {
            return new ModelAndView("redirect:/profile");
        }
        return new ModelAndView("signin.html");
    }

    @PostMapping("/signin")
    public ModelAndView doSignin(@RequestParam("email") String email, @RequestParam("password") String password, HttpServletResponse response, HttpSession session) throws IOException {
        try {
            User user = userService.signin(email, password);
            session.setAttribute(KEY_USER, user);
            messagingService.sendMailMessage(MailMessage.signin(user.getEmail(), user.getName()));
        } catch (Exception e) {
            Map<String, Object> map = new HashMap();
            map.put("email", email);
            map.put("error", "Register failed");
            return new ModelAndView("signin.html", map);
        }
        return new ModelAndView("redirect:/profile");
    }


    @GetMapping("/signout")
    public String signout(HttpSession session) {
        session.removeAttribute(KEY_USER);
        return "redirect:/signin";
    }

    @GetMapping("/profile")
    public ModelAndView profile(HttpSession session) {
        User user = (User) session.getAttribute(KEY_USER);
        if (user == null) {
            return new ModelAndView("redirect:/signin");
        }
        return new ModelAndView("/profile.html","user", user);
    }

    @ExceptionHandler(RuntimeException.class)
    public ModelAndView handleUnknowException(Exception ex) {
        Map<String,String> map = new HashMap<>();
        map.put("error", ex.getClass().getSimpleName());
        map.put("message", ex.getMessage());
        return new ModelAndView("500.html", map);
    }
}
