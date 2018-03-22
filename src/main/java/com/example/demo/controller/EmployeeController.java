package com.example.demo.controller;

import com.example.demo.Utils.ResultData;
import com.example.demo.model.Department;
import com.example.demo.model.Employee;
import com.example.demo.service.DepartmentService;
import com.example.demo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Controller
@RequestMapping("/emp")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private DepartmentService departmentService;

    /**
     * 路由，
     * @return
     */
    @RequestMapping("/{page}Page")
    public String routePage(@PathVariable String page, Integer id, Model model) {
        model.addAttribute("emp", employeeService.findOne(id).getData());
        model.addAttribute("depts", departmentService.findAll().getData());
        return page;
    }

    /**
     * 路由，单独接受参数
     * @return
     */
    @RequestMapping("/listPage")
    public ModelAndView listPage(Integer pageNo, Integer pageSize, String order, Employee employee) {
        if (null == pageNo) {
            pageNo = 0;
        }

        if (null == pageSize) {
            pageSize = 1;
        }

        ModelAndView mv = new ModelAndView("list");
        mv.addObject("pageNo", pageNo);
        mv.addObject("pageSize", pageSize);

        Employee plcce = new Employee();
        Department department = new Department();
        department.setLocation("");
        department.setName("");

        if (null == employee) {
            plcce.setSix(-1);
            plcce.setLevel(-1);
            plcce.setDepartment(department);
        } else if (null == employee.getDepartment()) {
            employee.setDepartment(department);
        }

        mv.addObject("emp", employee == null ? plcce : employee);
        mv.addObject("order", order == null ? "" : order);

//        result.put("dept", departmentDao.findAll());
//        result.put("emp_hoddy", employeeDao.findAllHoddy());
        Map map = (Map) employeeService.getQueryParams().getData();

        mv.addObject("dept", map.get("dept"));
        mv.addObject("emp_hoddy", map.get("emp_hoddy"));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String key = sdf.format(new Date());
        mv.addObject("allCount", LoginController.VISIT_COUNT.get(LoginController.ALLCOUNT_KEY));
        mv.addObject("todayCount", LoginController.VISIT_COUNT.get(key));

        return mv;
    }

    /**
     * 查询全部数据
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public ResultData list(int pageNo, int pageSize, String order, Employee employee) {
        pageNo = pageNo - 1 < 0 ? 0 : pageNo - 1;
        return employeeService.findPage(pageNo, pageSize, order, employee);
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

    /**
     * 删除数据
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    public ResultData delete(Integer id) {
        return employeeService.delete(id);
    }

    /**
     * 更新数据
     * @return
     */
    @RequestMapping("/save")
    @ResponseBody
    public ResultData update(Employee employee, MultipartFile headImg) {
        return employeeService.save(employee, headImg);
    }

    /**
     * 查找职工信息
     * @return
     */
    @RequestMapping("/findOne")
    @ResponseBody
    public ResultData update(Integer id) {
        return employeeService.findOne(id);
    }

    /**
     * 图片上传
     * @param uploadFile
     * @return
     */
    @RequestMapping("/uploadFile")
    @ResponseBody
    ResultData uploadPicture(MultipartFile uploadFile) {
        return employeeService.uploadPicture(uploadFile);
    }

    @RequestMapping("/modifyPwd")
    @ResponseBody
    public ResultData modifyPwd(Integer id, String pwd, String oldPwd) {
        return employeeService.modifyPwd(id, pwd, oldPwd);
    }

}
