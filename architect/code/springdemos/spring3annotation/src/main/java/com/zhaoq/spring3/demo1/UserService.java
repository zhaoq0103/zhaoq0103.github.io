package com.zhaoq.spring3.service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * 注解的方式装配Bean
 *
 */
// 在Spring配置文件中<bean id="userService" class="com.zhaoq.spring3.service.UserService">
// @Component("userService")
@Service(value="userService")
@Scope(value = "prototype")
public class UserService {
	@Value(value="www")
	private String info;
	
//	@Autowired(required=true)
//	@Qualifier("userDao")
	@Resource(name="userDao")
	private UserDao userDao;
	
	public void sayHello(){
		System.out.println("Hello Spring Annotation..."+info);
	}


	
	@PostConstruct
	public void setup(){
		System.out.println("初始化...");
	}
	
	@PreDestroy
	public void teardown(){
		System.out.println("销毁...");
	}
}
