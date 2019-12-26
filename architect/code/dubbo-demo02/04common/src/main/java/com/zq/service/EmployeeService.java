package com.zq.service;


import com.zq.bean.Employee;

public interface EmployeeService {
    void addEmployee(Employee employee);

    Employee findEmployeeById(int id);

    Integer findEmployeeCount();
}
