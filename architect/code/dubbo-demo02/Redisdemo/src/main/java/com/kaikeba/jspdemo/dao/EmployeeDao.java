package com.kaikeba.jspdemo.dao;

import com.kaikeba.jspdemo.bean.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * company: www.kaikeba.com
 * Author: Rey
 */
@Mapper
public interface EmployeeDao {
    void insertEmployee(Employee employee);

    Integer selectEmployeeCount();

    Employee selectEmployeeById(int id);
}
