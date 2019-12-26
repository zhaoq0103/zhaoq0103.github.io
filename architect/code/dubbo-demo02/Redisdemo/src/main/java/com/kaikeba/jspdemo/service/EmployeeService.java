package com.kaikeba.jspdemo.service;

import com.kaikeba.jspdemo.bean.Employee;

/**
 * company: www.kaikeba.com
 * Author: Rey
 */
public interface EmployeeService {
    void addEmployee(Employee employee);

    Employee findEmployeeById(int id);

    Integer findEmployeeCount();
}
