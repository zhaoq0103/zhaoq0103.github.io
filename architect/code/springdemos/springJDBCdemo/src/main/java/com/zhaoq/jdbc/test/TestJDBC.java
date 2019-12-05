package com.zhaoq.jdbc.test;

import org.junit.Test;

public class TestJDBC {

    @Test
    public void run1(){
        // 创建连接池，先使用Spring框架内置的连接池
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql:///spring ");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        // 创建模板类
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        // 完成数据的添加
        jdbcTemplate.update("insert into t_account values (null,?,?)", "测试",10000);
    }

}
