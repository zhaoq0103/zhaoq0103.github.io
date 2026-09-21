package com.ai.x.jdbc;

import com.ai.x.io.PropertyResolver;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

public class JdbcTest {

//    public static final String CREATE_USER = "CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(255) NOT NULL, age INTEGER)";
    public static final String CREATE_USER = "drop table IF  EXISTS users; CREATE TABLE IF NOT EXISTS users  (id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(255) NOT NULL, age INT);";
    public static final String CREATE_ADDRESS = "drop table IF  EXISTS addresses;CREATE TABLE IF NOT EXISTS addresses  (id INT PRIMARY KEY AUTO_INCREMENT, userId INTEGER NOT NULL, address VARCHAR(255) NOT NULL, zip INT)";

    public static final String INSERT_USER = "INSERT INTO users (name, age) VALUES (?, ?)";
    public static final String INSERT_ADDRESS = "INSERT INTO addresses (userId, address, zip) VALUES (?, ?, ?)";

    public static final String UPDATE_USER = "UPDATE users SET name = ?, age = ? WHERE id = ?";
    public static final String UPDATE_ADDRESS = "UPDATE addresses SET address = ?, zip = ? WHERE id = ?";

    public static final String DELETE_USER = "DELETE FROM users WHERE id = ?";
    public static final String DELETE_ADDRESS_BY_USERID = "DELETE FROM addresses WHERE userId = ?";

    public static final String SELECT_USER = "SELECT * FROM users WHERE id = ?";
    public static final String SELECT_USER_NAME = "SELECT name FROM users WHERE id = ?";
    public static final String SELECT_USER_AGE = "SELECT age FROM users WHERE id = ?";
    public static final String SELECT_ADDRESS_BY_USERID = "SELECT * FROM addresses WHERE userId = ?";

    @BeforeEach
    public void beforeEach() {
        cleanDb();
    }

    public PropertyResolver createSqlitePropertyResolver() {
        var ps = new Properties();
        ps.put("winter.datasource.url", "jdbc:sqlite:test.db");
        ps.put("winter.datasource.username", "sa");
        ps.put("winter.datasource.password", "");
        //用 sqlite 做测试
        ps.put("winter.datasource.driver-class-name", "org.sqlite.JDBC");
        var pr = new PropertyResolver(ps);
        return pr;
    }

    public PropertyResolver createPropertyResolver() {
       return createSqlitePropertyResolver();
    }

    public PropertyResolver createMysqlPropertyResolver() {
        var ps = new Properties();
        ps.put("winter.datasource.url", "jdbc:mysql://localhost/testdb?useSSL=false&allowMultiQueries=true&useUnicode=true&characterEncoding=utf8");
        ps.put("winter.datasource.username", "root");
        ps.put("winter.datasource.password", "123456");
        ps.put("winter.datasource.driver-class-name", "com.mysql.cj.jdbc.Driver");
        var pr = new PropertyResolver(ps);
        return pr;
    }

    void cleanDb() {
        Path db = Path.of("test.db").normalize().toAbsolutePath();
        try {
            Files.deleteIfExists(db);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}