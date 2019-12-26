package com.zhaoq.jdbc.trans;

public interface AccountDao {
    public void in(String from,Double money);
    public void out(String from,Double money);
}
