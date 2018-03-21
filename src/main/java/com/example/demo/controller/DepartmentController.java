package com.example.demo.controller;

import com.example.demo.Utils.ResultData;
import com.example.demo.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/dept")
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;

    /**
     * 查找全部部门
     * @return
     */
    @RequestMapping("/all")
    @ResponseBody
    public ResultData findAll() {
        return departmentService.findAll();
    }

}
