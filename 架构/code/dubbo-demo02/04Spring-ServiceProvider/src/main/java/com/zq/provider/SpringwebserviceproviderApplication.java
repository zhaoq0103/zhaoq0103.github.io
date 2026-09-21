package com.zq.provider;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubboConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement    // 开启事务
@SpringBootApplication
@EnableCaching                // 开启缓存
@EnableDubboConfig            // 开启Dubbo自动配置
public class SpringwebserviceproviderApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringwebserviceproviderApplication.class, args);
	}

}
