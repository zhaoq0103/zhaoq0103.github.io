package com.zhaoq.aop04;

import com.zhaoq.aop.CustomerDao;
import com.zhaoq.aop02.OrderDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext4.xml")
public class SpringTest6 {
	@Autowired
	@Qualifier("orderDao")
	private OrderDao orderDao;
	@Autowired
	@Qualifier("customerDao")
	private CustomerDao customerDao;
	
	@Test
	public void demo1(){
		orderDao.add();
		orderDao.update();
		orderDao.delete();
		
		customerDao.add();
	}
}
