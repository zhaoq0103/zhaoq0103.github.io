package com.x.ai.db.service;

import com.x.ai.db.entity.User;
import com.x.ai.db.mapper.UserMapper;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

@Transactional(propagation = Propagation.REQUIRED)
@Component
public class UserService {

    @Autowired
    UserMapper userMapper;


    public User fetchUserById(long id) {
        return userMapper.getById(id);
    }

    public User fetchUserByEmail(String email) {
        return userMapper.getByEmail(email);
    }

    public List<User> getAllUsers(int pageIndex) {
        int pageSize = 100;
        return userMapper.getAll((pageIndex - 1) * pageSize, pageSize);
    }

    public User register(String email, String password, String name) {
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

    public void updateUser(Long id, String name) {
        User user = fetchUserById(id);
        user.setName(name);
        user.setUpdateAt(System.currentTimeMillis());
        userMapper.update(user);
    }

    public User login(String email, String password) {
        return userMapper.getByEmail(email);
    }

}
