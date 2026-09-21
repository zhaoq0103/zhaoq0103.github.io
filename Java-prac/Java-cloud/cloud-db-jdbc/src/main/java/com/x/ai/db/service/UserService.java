package com.x.ai.db.service;

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

@Component
public class UserService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Transactional(propagation = Propagation.REQUIRED)
    public User register(String email, String password, String name) {
        KeyHolder holder = new GeneratedKeyHolder();
        if (1 != jdbcTemplate.update(
                // 参数1:PreparedStatementCreator
                (conn) -> {
                    // 创建PreparedStatement时，必须指定RETURN_GENERATED_KEYS:
                    PreparedStatement ps = conn.prepareStatement("INSERT INTO t_users(email, password, name) VALUES(?, ?, ?)",
                            Statement.RETURN_GENERATED_KEYS);
                    ps.setObject(1, email);
                    ps.setObject(2, password);
                    ps.setObject(3, name);
                    return ps;
                },
                // 参数2:KeyHolder
                holder)
        ) {
            throw new RuntimeException("Insert failed.");
        }
        return new User(holder.getKey().longValue(), email, password, name);
    }

    public long getUsers() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users", (ResultSet rs, int rowNum) -> {
            return rs.getLong(1);
        });
    }

    public void updateUser(User user) {
        // 传入SQL，SQL参数，返回更新的行数:
        if (1 != jdbcTemplate.update("UPDATE t_users SET name = ? WHERE id = ?", user.getName(), user.getId())) {
            throw new RuntimeException("User not found by id");
        }
    }

    public User getUserById(long id) {
        return jdbcTemplate.execute((Connection conn) -> {
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM t_users WHERE id = ?")){
                ps.setObject(1, id);
                try (java.sql.ResultSet rs = ps.executeQuery()){
                    if (rs.next()) {
                        return new User( // new User object:
                                rs.getLong("id"), // id
                                rs.getString("email"), // email
                                rs.getString("password"), // password
                                rs.getString("name")); // name
                    }
                    throw new RuntimeException("user not found by id.");
                }
            }

        });
    }

    public User getUserByName(String name) {
        return jdbcTemplate.queryForObject("SELECT * FROM t_users WHERE name = ?",(ResultSet rs, int rowNum) -> {
            return new User( // new User object:
                    rs.getLong("id"), // id
                    rs.getString("email"), // email
                    rs.getString("password"), // password
                    rs.getString("name")); // name

        },name);
    }

    public User getUserByEmail(String email) {
        return jdbcTemplate.execute("SELECT * FROM t_users WHERE email = ?", (PreparedStatement ps) -> {
                ps.setObject(1, email);
                try (java.sql.ResultSet rs = ps.executeQuery()){
                    if (rs.next()) {
                        return new User( // new User object:
                                rs.getLong("id"), // id
                                rs.getString("email"), // email
                                rs.getString("password"), // password
                                rs.getString("name")); // name
                    }
                    throw new RuntimeException("user not found by email.");
                }
            }
        );
    }
}
