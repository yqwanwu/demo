package com.example.demo.service.impl;

import com.example.demo.Utils.ResultData;
import com.example.demo.dao.DepartmentDao;
import com.example.demo.model.Department;
import com.example.demo.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentDao departmentDao;

    @Override
    public ResultData findAll() {
        try {
            return ResultData.ok(departmentDao.findAll());
        } catch (Exception e) {
            return ResultData.build(500, e.getMessage());
        }
    }
}
