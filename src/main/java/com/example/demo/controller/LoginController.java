package com.example.demo.controller;

import com.example.demo.Utils.CodeUtil;
import com.example.demo.Utils.ResultData;
import com.example.demo.model.Employee;
import com.example.demo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 登录控制器
 */
@Controller
@RequestMapping("/login")
public class LoginController {

    public static final String CODE_KEY = "code";
    public static final String SESSION_USER = "session.user";

    //记录访问数量 没时间用数据库来做了
    private static ConcurrentHashMap<String, Integer> VISIT_COUNT = new ConcurrentHashMap<>();

    @Autowired
    private EmployeeService employeeService;

    /**
     * 登录页面
     * @return
     */
    @RequestMapping("")
    public String loginPage() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String key = sdf.format(new Date());

        if (null == VISIT_COUNT.get(key)) {
            VISIT_COUNT.put(key, 1);
        } else {
            VISIT_COUNT.put(key, ((Integer)VISIT_COUNT.get(key)) + 1);
        }

        return "login";
    }


    /**
     * 注册页面
     * @return
     */
    @RequestMapping("/registerPage")
    public String registerPage() {
        return "register";
    }

    /**
     * 注册
     * @param employee
     * @param code
     * @param request
     * @return
     */
    @RequestMapping("/register")
    @ResponseBody
    public ResultData register(Employee employee, String code, HttpServletRequest request) {
        //检测验证码
        String sessionCode = (String) request.getSession().getAttribute(LoginController.CODE_KEY);
        if (!sessionCode.equals(code)) {
            return ResultData.build(9001, "验证码错误");
        }

        try {
            employeeService.add(employee);
        } catch (Exception e) {
            return ResultData.build(500, e.getMessage());
        }
        return ResultData.build(200, "注册成功, 请点击登录");
    }


    /**
     * 登录数据验证
     * @return
     */
    @RequestMapping("/checkLoginData")
    @ResponseBody
    public ResultData checkLogin(String name, String pwd, String code, HttpServletRequest request) {
        String sessionCode = (String) request.getSession().getAttribute(LoginController.CODE_KEY);
        if (!sessionCode.equals(code)) {
            return ResultData.build(9001, "验证码错误");
        }

        try {
            Employee employee = employeeService.login(name, pwd);
            request.getSession().setAttribute(LoginController.SESSION_USER, employee);
        } catch (Exception e) {
            return ResultData.build(9000, e.getMessage());
        }

        return ResultData.ok();
    }


    /**
     * 获取验证码图片
     * @param resp
     * @param req
     */
    @RequestMapping("/getCode")
    public void getCodeImg(HttpServletResponse resp, HttpServletRequest req) {
        // 调用工具类生成的验证码和验证码图片
        Map<String, Object> codeMap = CodeUtil.generateCodeAndPic();

        // 将四位数字的验证码保存到Session中。
        HttpSession session = req.getSession();
        session.setAttribute(LoginController.CODE_KEY, codeMap.get("code").toString());

        // 禁止图像缓存。
        resp.setHeader("Pragma", "no-cache");
        resp.setHeader("Cache-Control", "no-cache");
        resp.setDateHeader("Expires", -1);

        resp.setContentType("image/jpeg");

        // 将图像输出到Servlet输出流中。
        ServletOutputStream sos;
        try {
            sos = resp.getOutputStream();
            ImageIO.write((RenderedImage) codeMap.get("codePic"), "jpeg", sos);
            sos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 退出登录
     * @return
     */
    @RequestMapping("/logout")
    @ResponseBody
    public ResultData logout(HttpServletRequest request) {
        request.getSession().removeAttribute(LoginController.SESSION_USER);
        return ResultData.ok();
    }

}
