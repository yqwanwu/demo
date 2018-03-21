package com.example.demo.controller;

import com.example.demo.Utils.ResultData;
import com.example.demo.model.Employee;
import com.example.demo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

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
        return employeeService.findPage();
    }

    /**
     * 获取用户信息
     * @return
     */
    @RequestMapping("/userInfo")
    @ResponseBody
    public ResultData getUserInfo(HttpServletRequest request) {
        Object obj = request.getSession().getAttribute(LoginController.SESSION_USER);
        return ResultData.ok(obj);
    }

    /**
     * 查询全部列表页面需要的查询条件
     * @return
     */
    @RequestMapping("/getQueryParams")
    @ResponseBody
    public ResultData getQueryParams() {
        return employeeService.getQueryParams();
    }

}
