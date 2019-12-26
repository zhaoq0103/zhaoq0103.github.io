package com.zhaoq.jdbc.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")

public class TestJDBC {
//
//    @Test
//    public void run1(){
//        // 创建连接池，先使用Spring框架内置的连接池
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
//        dataSource.setUrl("jdbc:mysql:///test");
//        dataSource.setUsername("root");
//        dataSource.setPassword("123456");
//        // 创建模板类
//        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
//        // 完成数据的添加
//        jdbcTemplate.update("insert into zq_user values (null, ?,?,?,?,?)","wo","112233",19, 2,"2019-01-01");
//    }


//    @Autowired
//    @Qualifier("jdbcTemplate")
//    private JdbcTemplate jdbcTemplate;
//
//    @Test
//    public void run2(){
//        jdbcTemplate.execute("create table user (id int primary key auto_increment,name varchar(20))");
//        jdbcTemplate.execute("insert into user values (null , 'qitiandasheng');");
//    }




    @Autowired
//    @Qualifier("userDao")
    private UserDAO userDao;


    @Test
    public void run3(){
//        User user = new User();
//        user.setName("小胖2");
//
//        this.userDao.add(user);
//
//        User user2 = new User();
//        user2.setId(20);
//        user2.setName("小边20");
//        userDao.add(user2);
//
//        User user3 = userDao.findById(4);
//        System.out.println(user);

        List<User> list = userDao.findAll();
        for (User tmpuser : list) {
            System.out.println(tmpuser);
        }
    }

}
