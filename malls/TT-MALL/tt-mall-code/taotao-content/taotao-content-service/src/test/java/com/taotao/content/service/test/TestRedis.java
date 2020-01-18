package com.taotao.content.service.test;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import com.taotao.content.service.jedis.JedisClient;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

public class TestRedis {
	
	@Test
	public void testhelper(){
		
		//2.初始化spring 容器
//		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-dao.xml");

		Jedis j = new Jedis("redisOS", 6379);
		j.set("mykey", "zhaoq0103");
		System.out.println(j.get("mykey"));
		j.close();
	}


	@Test
	public void testPool(){
		JedisPool pool = new JedisPool("redisOS", 6379);
		Jedis j =  pool.getResource();
		j.set("mykey2", "zhaoq01032");
		System.out.println(j.get("mykey2"));
		j.close();
		pool.close();
	}


    @Test
    public void testjediscluster() throws IOException {
        Set<HostAndPort> nodes = new HashSet<>();
        nodes.add(new HostAndPort("redisOS", 7001));
        nodes.add(new HostAndPort("redisOS", 7002));
        nodes.add(new HostAndPort("redisOS", 7003));
        nodes.add(new HostAndPort("redisOS", 7004));
        nodes.add(new HostAndPort("redisOS", 7005));
        nodes.add(new HostAndPort("redisOS", 7006));
        //1.创建jediscluster对象
        JedisCluster cluster = new JedisCluster(nodes);
        //2.直接根据jediscluster对象操作redis集群
        cluster.set("keycluster", "cluster");
        System.out.println(cluster.get("keycluster"));
        //3.关闭jediscluster对象(是在应用系统关闭的时候关闭) 封装了连接池
        cluster.close();
    }


    @Test
    public void testdanji(){
        //1.初始化spring容器
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");
        //2.获取实现类实例
        JedisClient bean = context.getBean(JedisClient.class);
        //3.调用方法操作
        bean.set("jedisclientkey11", "jedisclientkey");
        System.out.println(bean.get("jedisclientkey11"));
    }
}
