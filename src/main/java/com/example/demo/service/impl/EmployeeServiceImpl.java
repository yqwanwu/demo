package com.example.demo.service.impl;

import com.example.demo.Utils.ResultData;
import com.example.demo.Utils.Tools;
import com.example.demo.dao.EmployeeDao;
import com.example.demo.model.Employee;
import com.example.demo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 方法注释在 接口层
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeDao employeeDao;

    @Override
    public Employee login(String name, String pwd) {
        Employee result = employeeDao.findOneByNameAndAndPwd(name, Tools.encoderByMd5(pwd));

        if (null == result) {
            throw new RuntimeException("用户名或密码错误");
        }

        return result;
    }

    @Override
    public void add(Employee employee) {
        if (StringUtils.isEmpty(employee.getName()) || StringUtils.isEmpty(employee.getPwd())) {
            throw new RuntimeException("用户名和密码不能为空");
        }

        if (employee.getDepartment() == null) {
            throw new RuntimeException("请选择部门");
        }

        if (null != employeeDao.findOneByName(employee.getName())) {
            throw new RuntimeException("用户已存在");
        }

        employee.setPwd(Tools.encoderByMd5(employee.getPwd()));
        employeeDao.save(employee);
    }

    @Override
    public ResultData findAll() {
        try {
            return ResultData.ok(employeeDao.findAll());
        } catch (Exception e) {
            return ResultData.build(500, e.getMessage());
        }

    }
}
