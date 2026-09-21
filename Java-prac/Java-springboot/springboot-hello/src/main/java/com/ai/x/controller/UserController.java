package com.ai.x.controller;

import com.ai.x.config.RoutingWithSlave;
import com.ai.x.entity.User;
import com.ai.x.service.RedisService;
import com.ai.x.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    RedisService redisService;
    @Autowired
    ObjectMapper objectMapper;

//    @Autowired
//    MessagingService messagingService;
    public static final String KEY_USER = "__user__";
    public static final String KEY_USER_ID = "__userid__";

    @GetMapping("/")
    public ModelAndView index(HttpSession session) throws Exception {
//        User user = (User) session.getAttribute(KEY_USER);
        User user = getUserFromRedis(session);
        Map<String, Object> model = new HashMap<>();
        if (user != null) {
            model.put("user", model);
        }
        return new ModelAndView("index.html", model);
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
//            messagingService.sendMailMessage(MailMessage.registration(user.getEmail(), user.getName()));
        } catch (Exception e) {
            Map<String, Object> map = new HashMap();
            map.put("email", email);
            map.put("error", "Register failed");

            return new ModelAndView("register.html", map);
        }
        return new ModelAndView("redirect:/signin");
    }


    @GetMapping("/signin")
    public ModelAndView signin(HttpSession session) throws Exception{
//        User user = (User) session.getAttribute(KEY_USER);
        User user = getUserFromRedis(session);
        if (user != null) {
            return new ModelAndView("redirect:/profile");
        }
        return new ModelAndView("signin.html");
    }

    @PostMapping("/signin")
    public ModelAndView doSignin(@RequestParam("email") String email, @RequestParam("password") String password, HttpServletResponse response, HttpSession session) throws IOException {
        try {
            User user = userService.signin(email, password);
            session.setAttribute(KEY_USER_ID, user.getId());
//            session.setAttribute(KEY_USER, user);
//            messagingService.sendMailMessage(MailMessage.signin(user.getEmail(), user.getName()));
            putUserIntoRedis(user);
        } catch (Exception e) {
            Map<String, Object> map = new HashMap();
            map.put("email", email);
            map.put("error", "Register failed");
            return new ModelAndView("signin.html", map);
        }
        return new ModelAndView("redirect:/profile");
    }

    @RoutingWithSlave
    @GetMapping("/profile")
    public ModelAndView profile(HttpSession session) throws Exception {
//        User user = (User) session.getAttribute(KEY_USER);
        User user = getUserFromRedis(session);
        if (user == null) {
            return new ModelAndView("redirect:/signin");
        }

        // 测试是否走slave数据库:
        user = userService.getUserByEmail(user.getEmail());
        return new ModelAndView("/profile.html","user", user);
    }

    @GetMapping("/signout")
    public String signout(HttpSession session) {
//        session.removeAttribute(KEY_USER);
        session.removeAttribute(KEY_USER_ID);
        return "redirect:/signin";
    }

    @ExceptionHandler(RuntimeException.class)
    public ModelAndView handleUnknowException(Exception ex) {
        Map<String,String> map = new HashMap<>();
        map.put("error", ex.getClass().getSimpleName());
        map.put("message", ex.getMessage());
        return new ModelAndView("500.html", map);
    }


    private void putUserIntoRedis(User user) throws Exception {
        redisService.hset(KEY_USER, user.getId().toString(), objectMapper.writeValueAsString(user));
    }

    // 从Redis读取User:
    private User getUserFromRedis(HttpSession session) throws Exception {
        Long id = (Long) session.getAttribute(KEY_USER_ID);
        if (id != null) {
            String s = redisService.hget(KEY_USER, id.toString());
            if (s != null) {
                return objectMapper.readValue(s, User.class);
            }
        }
        return null;
    }
}
