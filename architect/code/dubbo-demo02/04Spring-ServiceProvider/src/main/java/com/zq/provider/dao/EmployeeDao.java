package com.zq.provider.dao;


import com.zq.bean.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeDao {
    void insertEmployee(Employee employee);

    Integer selectEmployeeCount();

    Employee selectEmployeeById(int id);
}
