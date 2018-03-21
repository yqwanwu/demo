package com.example.demo.controller;

import com.example.demo.Utils.ResultData;
import com.example.demo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/emp")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 路由，
     * @return
     */
    @RequestMapping("/{page}Page")
    public String routePage(@PathVariable String page) {
        return page;
    }

    /**
     * 查询全部数据
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public ResultData list() {
        return employeeService.findAll();
    }

}
