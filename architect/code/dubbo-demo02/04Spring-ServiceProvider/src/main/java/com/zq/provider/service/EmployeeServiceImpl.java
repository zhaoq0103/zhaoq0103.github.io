package com.zq.provider.service;

import com.alibaba.dubbo.config.annotation.Service;

import com.zq.bean.Employee;
import com.zq.provider.dao.EmployeeDao;
import com.zq.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;


@Service
@Component
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeDao dao;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @CacheEvict(value="realTimeCache", allEntries = true)
    @Transactional
    @Override
    public void addEmployee(Employee employee) {
        dao.insertEmployee(employee);
        // int i = 3 / 0;
        // dao.insertEmployee(employee);
    }

    @Cacheable(value="realTimeCache")
    @Override
    public Employee findEmployeeById(int id) {
        return dao.selectEmployeeById(id);
    }

    // 使用双重检测锁解决热点缓存问题
    @Override
    public Integer findEmployeeCount() {
        // 获取Redis操作对象
        BoundValueOperations<Object, Object> ops = redisTemplate.boundValueOps("count");
        // 从缓存中读取数据
        Object count = ops.get();
        if(count == null) {
           synchronized (this) {
                count = ops.get();
                if(count == null) {
                    // 从DB中查询
                    count = dao.selectEmployeeCount();
                    // 将查询的数据写入到Redis缓存，并设置到期时限
                    ops.set(count, 10, TimeUnit.SECONDS);
                }
            }
        }
        return (Integer) count;
    }
}
