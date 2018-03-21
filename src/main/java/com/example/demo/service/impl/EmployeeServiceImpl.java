package com.example.demo.service.impl;

import com.example.demo.Utils.ResultData;
import com.example.demo.Utils.Tools;
import com.example.demo.dao.DepartmentDao;
import com.example.demo.dao.EmployeeDao;
import com.example.demo.model.Employee;
import com.example.demo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 方法注释在 接口层
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeDao employeeDao;
    @Autowired
    private DepartmentDao departmentDao;

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

    public ResultData findPage() {
        try {
            //QBC
            PageRequest pageRequest = new PageRequest(0, 3);
            Page<Employee> list = employeeDao.findAll(new Specification<Employee>() {
                @Override
                public Predicate toPredicate(Root<Employee> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    Predicate pHobby = criteriaBuilder.equal(root.get("hobby").as(String.class), "数学家");
                    return criteriaBuilder.and(pHobby);
                }
            }, pageRequest);

            return ResultData.ok(list);
        } catch (Exception e) {
            return ResultData.build(500, e.getMessage());
        }
    }

    @Override
    public ResultData getQueryParams() {
        try {
            Map<String, List> result = new HashMap<>();
            result.put("dept", departmentDao.findAll());
            result.put("emp_hoddy", employeeDao.findAllHoddy());
            return ResultData.ok(result);
        } catch (Exception e) {
            return ResultData.build(500, e.getMessage());
        }
    }
}
