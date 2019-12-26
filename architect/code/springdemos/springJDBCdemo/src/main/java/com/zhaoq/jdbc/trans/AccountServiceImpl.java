package com.zhaoq.jdbc.trans;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED,readOnly = false)
public class AccountServiceImpl implements AccountService{
    private TransactionTemplate transactionTpl;
    private AccountDao accountDao;

    //没有setter方法就不能在XML中注入
    public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
        this.transactionTpl = transactionTemplate;
    }

    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    public void transfer(String from, String to, Double money) {

//        transactionTpl.execute(new TransactionCallbackWithoutResult() {
//            @Override
//            protected void doInTransactionWithoutResult(TransactionStatus status) {
                accountDao.out(from, money);
                int d = 1 / 0;
                accountDao.in(to, money);
//            }
//        });

    }
}
