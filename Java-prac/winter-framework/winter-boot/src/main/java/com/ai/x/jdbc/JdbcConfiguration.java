package com.ai.x.jdbc;

import com.ai.x.annotation.Autowired;
import com.ai.x.annotation.Bean;
import com.ai.x.annotation.Configuration;
import com.ai.x.annotation.Value;
import com.ai.x.jdbc.tx.DataSourceTransactionManager;
import com.ai.x.jdbc.tx.PlatformTransactionManager;
import com.ai.x.jdbc.tx.TransactionalBeanPostProcessor;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

@Configuration
public class JdbcConfiguration {
    @Bean(destroyMethod = "close")
    DataSource dataSource(
            // properties:
            @Value("${winter.datasource.url}") String url, //
            @Value("${winter.datasource.username}") String username, //
            @Value("${winter.datasource.password}") String password, //
            @Value("${winter.datasource.driver-class-name:}") String driver, //
            @Value("${winter.datasource.maximum-pool-size:20}") int maximumPoolSize, //
            @Value("${winter.datasource.minimum-pool-size:1}") int minimumPoolSize, //
            @Value("${winter.datasource.connection-timeout:30000}") int connTimeout //
    ) {
        var config = new HikariConfig();
        config.setAutoCommit(false);
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        if (driver != null) {
            config.setDriverClassName(driver);
        }
        config.setMaximumPoolSize(maximumPoolSize);
        config.setMinimumIdle(minimumPoolSize);
        config.setConnectionTimeout(connTimeout);
        return new HikariDataSource(config);
    }

    @Bean
    JdbcTemplate jdbcTemplate(@Autowired DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    TransactionalBeanPostProcessor transactionalBeanPostProcessor() {
        return new TransactionalBeanPostProcessor();
    }

    @Bean
    PlatformTransactionManager platformTransactionManager(@Autowired DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
