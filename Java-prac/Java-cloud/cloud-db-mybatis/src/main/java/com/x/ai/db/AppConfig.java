package com.x.ai.db;

import com.x.ai.db.entity.User;
import com.x.ai.db.service.UserService;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.SessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;


import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@ComponentScan
@EnableTransactionManagement // 启用声明式
@PropertySource("jdbc.properties")
@MapperScan("com.x.ai.db.mapper")
public class AppConfig {

    @Bean
    DataSource createDataSource(@Value("${jdbc.url}") String jdbcUrl, @Value("${jdbc.username}") String jdbcUsername, @Value("${jdbc.password}") String jdbcPassword) {

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(jdbcUsername);
        config.setPassword(jdbcPassword);
        config.addDataSourceProperty("autoCommit", "true");
        config.addDataSourceProperty("connectionTimeout", "5");
        config.addDataSourceProperty("idleTimeout", "60");
        return new HikariDataSource(config);
    }

    @Bean
    PlatformTransactionManager createTxManager(@Autowired DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    SqlSessionFactoryBean createSqlSessionFactoryBean(@Autowired DataSource dataSource) {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        return sqlSessionFactoryBean;
    }

    public static void  main(String[] args){
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        try{
            UserService userService = context.getBean(UserService.class);

            if (userService.fetchUserByEmail("dog@example.com") == null) {
                User bob = userService.register("dog@example.com", "bob123", "Dog");
                System.out.println("Registered ok: " + bob);
            }
            if (userService.fetchUserByEmail("egg@example.com") == null) {
                User alice = userService.register("egg@example.com", "egg", "Egg");
                System.out.println("Registered ok: " + alice);
            }
            // 查询所有用户:
            for (User u : userService.getAllUsers(1)) {
                System.out.println(u);
            }
            User bob = userService.login("bob@example.com", "bob123");
            System.out.println(bob);

            userService.updateUser(Long.valueOf(1),"microEgg");
            User microBob = userService.fetchUserById(1);
            if (microBob != null){
                System.out.println("microEgg: " + microBob);
            }
        }catch (Exception e ){
            System.err.println(e);
        }finally {
            ((ConfigurableApplicationContext) context).close();
        }
    }
}
