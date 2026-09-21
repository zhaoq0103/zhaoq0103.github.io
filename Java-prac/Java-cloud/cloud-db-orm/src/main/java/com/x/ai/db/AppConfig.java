package com.x.ai.db;

import com.x.ai.db.entity.User;
import com.x.ai.db.service.UserService;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
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
    PlatformTransactionManager createTxManager(@Autowired SessionFactory sessionFactory) {
        return new HibernateTransactionManager(sessionFactory);
    }

    @Bean
    LocalSessionFactoryBean createSessionFactory(@Autowired DataSource dataSource) {
        Properties props = new Properties();
        props.setProperty("hibernate.hbm2ddl.auto", "update"); // 生产环境不要使用
        props.setProperty("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
//        props.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        props.setProperty("hibernate.show_sql", "true");
        LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        // 扫描指定的package获取所有entity class:
        sessionFactoryBean.setPackagesToScan("com.x.ai.db.entity");
        sessionFactoryBean.setHibernateProperties(props);
        return sessionFactoryBean;
    }

    public static void  main(String[] args){
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        try{
            UserService userService = context.getBean(UserService.class);

            if (userService.fetchUserByEmail("bob@example.com") == null) {
                User bob = userService.register("bob@example.com", "bob123", "Bob");
                System.out.println("Registered ok: " + bob);
            }
            if (userService.fetchUserByEmail("alice@example.com") == null) {
                User alice = userService.register("alice@example.com", "helloalice", "Bob");
                System.out.println("Registered ok: " + alice);
            }
            // 查询所有用户:
            for (User u : userService.getUsers(1)) {
                System.out.println(u);
            }
            User bob = userService.login("bob@example.com", "bob123");
            System.out.println(bob);

            userService.updateUser(Long.valueOf(1),"microBob2");
            User microBob = userService.fetchUserById(1);
            if (microBob != null){
                System.out.println("microBob2: " + microBob);
            }
        }catch (Exception e ){
            System.err.println(e);
        }finally {
            ((ConfigurableApplicationContext) context).close();
        }
    }
}
