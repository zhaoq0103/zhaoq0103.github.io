package com.zq.consumer.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zq.bean.Employee;
import com.zq.service.EmployeeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: zhaoq0103
 * @Description:
 * @Date:Create：in 2019/12/26 9:58 AM
 * @Modified By：zhaoq0103
 */


@Controller
@RequestMapping("/home")
public class HomeController {
    // @Autowired
    @Reference
    private EmployeeService employeeService;

    @PostMapping("/register")
    public String someHandle(Employee employee) {
        employeeService.addEmployee(employee);
        return "page/welcome";
    }

    @RequestMapping("/find")
    @ResponseBody
    public Employee findHandle(int id) {
        return employeeService.findEmployeeById(id);
    }

    @RequestMapping("/count")
    @ResponseBody
    public Integer countHandle() {
//        return 666;
        return employeeService.findEmployeeCount();
    }
}
