package com.x.ai.db;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.SQLException;

@Component
public class DatabaseInitializer {

    @Autowired
    DataSource dataSource;

    @PostConstruct
    public void init() throws SQLException {
        try (java.sql.Connection conn = dataSource.getConnection()) {
            try (java.sql.Statement stmt = conn.createStatement()) {
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS t_users (" //
                        + "id BIGINT IDENTITY NOT NULL PRIMARY KEY, " //
                        + "email VARCHAR(100) NOT NULL, " //
                        + "password VARCHAR(100) NOT NULL, " //
                        + "name VARCHAR(100) NOT NULL, " //
                        + "createdAt BIGINT NOT NULL, " //
                        + "updateAt BIGINT NOT NULL, "
                        + "UNIQUE (email))");
            }
        }
    }
}
