package com.zhaoq.jdbc.trans;

import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class AccountDaoImpl extends JdbcDaoSupport implements AccountDao {

    @Override
    public void in(String from, Double money) {

        String sql = "update account set money = money + ? where name  = ?";
        this.getJdbcTemplate().update(sql, money , from);
    }

    @Override
    public void out(String to, Double money) {

        String sql = "update account set money = money - ? where name  = ?";
        this.getJdbcTemplate().update(sql, money,to);
    }
}
