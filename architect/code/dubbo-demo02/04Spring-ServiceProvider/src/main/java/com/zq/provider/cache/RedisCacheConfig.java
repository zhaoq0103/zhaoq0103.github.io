package com.zq.provider.cache;

import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisCacheConfig extends CachingConfigurerSupport {

    // 自动生成的key结构：类名_方法名_参数值
    // 本例：EmployeeServiceImpl_findEmployeeById_5
    @Override
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            // 获取注解所标的方法所在的类的类名
            String className = target.getClass().getName();
            // 获取注解所标注的方法的方法名
            String methodName = method.getName();
            return className + "_" + methodName + "_" + params[0].toString();
        };
    }

}
