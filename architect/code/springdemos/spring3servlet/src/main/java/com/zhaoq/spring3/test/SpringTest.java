package com.zhaoq.spring3.test;

import com.zhaoq.spring3.service.PersonService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class SpringTest {
	@Autowired
	private PersonService userService;
	
	@Test
	public void demo1(){
		userService.sayHello();
	}
}
