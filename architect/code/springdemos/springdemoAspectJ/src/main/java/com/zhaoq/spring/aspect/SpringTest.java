package com.zhaoq.spring.aspect;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:aopConfig.xml")
public class SpringTest {
	@Autowired
	@Qualifier("userService")
	private UserService userService;
	
	@Test
	public void demo1(){
		userService.add();
		userService.delete();
		userService.find();
		userService.update();
	}
}
