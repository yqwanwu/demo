package com.example.demo.service;

import com.example.demo.Utils.ResultData;
import com.example.demo.model.Employee;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

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

    /**
     * 分页查询
     * @return
     */
    ResultData findPage(int pageNo, int pageSize, String order, Employee employee);

    /**
     * 查询列表页面需要的查询条件
     * @return
     */
    ResultData getQueryParams();

    /**
     * 根据id 删除
     * @param id
     * @return
     */
    ResultData delete(Integer id);

    /**
     * 更新数据
     * @param employee
     * @return
     */
    ResultData save(Employee employee, MultipartFile headImg);

    /**
     * 根据id查找
     * @param id
     * @return
     */
    ResultData findOne(Integer id);

    /**
     * 图片上传
     * @param uploadFile
     * @return
     */
    ResultData uploadPicture(MultipartFile uploadFile);

    ResultData modifyPwd(Integer id, String pwd, String oldPwd);
}
