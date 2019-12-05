package com.zhaoq.spring.test;


import com.zhaoq.spring.service.UserService;
import com.zhaoq.spring.service.UserServiceImpl;
import com.zhaoq.spring.utils.ProxyUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
//locations：XML方式时，读取配置文件
@ContextConfiguration(locations = "classpath:aopConfig.xml")
public class UserServiceTest03 {

    @Autowired
    private UserService userService;

    @Test
    public void testJDKProxy(){

        UserService proxy = ProxyUtil.getProxyService(new UserServiceImpl());
        proxy.saveUser();

    }



    @Test
    public void testGclibProxy(){
        UserService proxy = ProxyUtil.getGCLibProxyService(new UserServiceImpl());
        proxy.saveUser();
    }

    /**
     * demo3和demo2用了相同的包名和类名，导致初始化失败，Remove Module demo2就可以了
     */
    @Test
    public void testUserAOP(){
      this.userService.saveUser();
    }
}
