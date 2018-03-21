package com.example.demo.service;

import com.example.demo.Utils.ResultData;
import com.example.demo.model.Employee;

public interface EmployeeService {
    /**
     *
     * @param name 用户名
     * @param pwd 密码
     * @return 登录成功后返回实体
     */
    Employee login(String name, String pwd);

    /**
     * 添加新的职工
     * @param employee
     */
    void add(Employee employee);

    /**
     * 查询全部数据
     * @return
     */
    ResultData findAll();
}
