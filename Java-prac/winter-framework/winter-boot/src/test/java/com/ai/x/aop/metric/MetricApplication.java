package com.ai.x.aop.metric;


import com.ai.x.annotation.ComponentScan;
import com.ai.x.annotation.Configuration;

@Configuration
@ComponentScan
public class MetricApplication {
// 这里的 MetricProxyBeanPostProcessor 直接Component 注解生成，不用再手动写
}
