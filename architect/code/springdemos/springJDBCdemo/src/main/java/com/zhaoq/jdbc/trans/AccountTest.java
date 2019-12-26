package com.zhaoq.jdbc.trans;

import junit.framework.JUnit4TestCaseFacade;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext2.xml")
public class AccountTest {
    @Autowired
    @Qualifier("accountService")
    private AccountService accountService;

    @Test
    public void test1(){
        accountService.transfer("aaa", "bbb", 100d);
    }
}
