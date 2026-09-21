package com.x.ai.service;

import com.x.ai.entity.User;
import com.x.ai.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Transactional(propagation = Propagation.REQUIRED)
@Component
public class UserService {

    final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    UserMapper userMapper;


    public User getUserById(long id) {
        return userMapper.getById(id);
    }

    public User getUserByEmail(String email) {
        return userMapper.getByEmail(email);
    }


    public User signin(String email, String password) {
        logger.info("try login by {}...", email);
        User user = getUserByEmail(email);
        if (user.getPassword().equals(password)) {
            return user;
        }
        throw new RuntimeException("login failed.");
    }

    public List<User> getAllUsers(int pageIndex) {
        int pageSize = 100;
        return userMapper.getAll((pageIndex - 1) * pageSize, pageSize);
    }

    public User register(String email, String password, String name) {
        logger.info("try register by {}...", email);

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setName(name);
        user.setCreatedAt(System.currentTimeMillis());
        user.setUpdateAt(System.currentTimeMillis());
        userMapper.insert(user);
        return user;
    }

    public void deleteUser(Long id) {
       userMapper.deleteById(id);
    }

    public void updateUser(User user) {
        if (user != null){
            User u = getUserById(user.getId());
            u.setName(user.getName());
            u.setUpdateAt(System.currentTimeMillis());
            userMapper.update(user);
        }
    }

    public User login(String email, String password) {
        return userMapper.getByEmail(email);
    }

}
