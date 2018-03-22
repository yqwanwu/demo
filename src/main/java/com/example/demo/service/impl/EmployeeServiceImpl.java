package com.example.demo.service.impl;

import com.example.demo.Utils.ResultData;
import com.example.demo.Utils.Tools;
import com.example.demo.dao.DepartmentDao;
import com.example.demo.dao.EmployeeDao;
import com.example.demo.model.Employee;
import com.example.demo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 方法注释在 接口层
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeDao employeeDao;
    @Autowired
    private DepartmentDao departmentDao;

    @Value("${imgHome}")
    private String imgHome;

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

        employee.setLevel(1);
        employee.setSix(1);
        employee.setHobby("游泳");
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

    @Override
    public ResultData findPage(int pageNo, int pageSize, String order, Employee employee) {
        try {
            //QBC
            PageRequest pageRequest = new PageRequest(pageNo, pageSize);
            Page<Employee> list = employeeDao.findAll(new Specification<Employee>() {
                @Override
                public Predicate toPredicate(Root<Employee> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    List<Predicate> ps = new ArrayList<>();
                    if (null != employee) {
                        if (!StringUtils.isEmpty(employee.getHobby())) {
                            Predicate p = criteriaBuilder.equal(root.get("hobby").as(String.class), employee.getHobby());
                            ps.add(p);
                        }

                        if (!StringUtils.isEmpty(employee.getUname())) {
                            Predicate p = criteriaBuilder.like(root.get("uname").as(String.class), "%" + employee.getUname() + "%");
                            ps.add(p);
                        }

                        if (null != employee.getDepartment() && !StringUtils.isEmpty(employee.getDepartment().getLocation())) {
                            Predicate p = criteriaBuilder.equal(root.get("department").get("location").as(String.class), employee.getDepartment().getLocation());
                            ps.add(p);
                        }
                        if (null != employee.getDepartment() && !StringUtils.isEmpty(employee.getDepartment().getName())) {
                            Predicate p = criteriaBuilder.equal(root.get("department").get("name").as(String.class), employee.getDepartment().getName());
                            ps.add(p);
                        }

                        if (employee.getSix() > 0) {
                            Predicate p = criteriaBuilder.equal(root.get("six").as(Integer.class), employee.getSix());
                            ps.add(p);
                        }
                        if (employee.getLevel() > 0) {
                            Predicate p = criteriaBuilder.equal(root.get("level").as(Integer.class), employee.getLevel());
                            ps.add(p);
                        }
                    }

                    if ("desc".equals(order)) {
                        Predicate p = criteriaQuery.orderBy(criteriaBuilder.desc(root.get("wages"))).getRestriction();
                    }

                    return criteriaBuilder.and(ps.toArray(new Predicate[ps.size()]));
                }
            }, pageRequest);

            if (list == null || list.getTotalElements() < 1) {
                return ResultData.build(9004, "无数据");
            }

            return ResultData.ok(list);
        } catch (Exception e) {
            e.printStackTrace();
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

    @Override
    public ResultData delete(Integer id) {
        try {
            employeeDao.delete(id);
            return ResultData.ok();
        } catch (Exception e) {
            return ResultData.build(500, e.getMessage());
        }
    }

    @Override
    public ResultData save(Employee employee, MultipartFile headImg) {
        try {
            if (employee.getId() != 0) {
                Employee old = employeeDao.findOne(employee.getId());
                if (null != old) {
                    employee.setPwd(old.getPwd());
                }
            } else {
                employee.setPwd(Tools.encoderByMd5(employee.getPwd()));
                Employee result = employeeDao.findOneByName(employee.getName());
                if (result != null) {
                    return ResultData.build(500, "用户名重复");
                }
            }

            if (StringUtils.isEmpty(employee.getPwd()) || StringUtils.isEmpty(employee.getName())) {
                return ResultData.build(500, "用户名密码不能为空");
            }

            employeeDao.save(employee);
            return ResultData.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultData.build(500, e.getMessage());
        }
    }

    //上传成功后返回图片路径
    @Override
    public ResultData uploadPicture(MultipartFile uploadFile) {
        FileOutputStream fo = null;
        try {
            String oldName = uploadFile.getOriginalFilename();
            String name = UUID.randomUUID() + oldName.substring(oldName.lastIndexOf("."));
            String dirStr = new SimpleDateFormat("MM/dd").format(new Date());
            String fullDir = imgHome + dirStr;
            File dir = new File(fullDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            fo = new FileOutputStream(fullDir + "/" + name);
//            byte[] buf = new byte[1024];
//            while (uploadFile.getInputStream().read(buf) != -1) {
//                fo.write(buf);
//                fo.flush();
//            }

            fo.write(uploadFile.getBytes());
            fo.flush();

            return ResultData.ok("/img/" + dirStr + "/" + name);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultData.build(500, e.getMessage());
        } finally {
            try {
                if (null != fo) {
                    fo.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public ResultData findOne(Integer id) {
        try {
            return ResultData.ok(employeeDao.findOne(id));
        } catch (Exception e) {
            return ResultData.build(500, e.getMessage());
        }
    }

    @Override
    public ResultData modifyPwd(Integer id, String pwd, String oldPwd) {
        try {
            Employee employee = employeeDao.findOne(id);
            if (null == employee) {
                return ResultData.build(500, "没用该用户");
            }

            if (oldPwd == null || !Tools.encoderByMd5(oldPwd).equals(employee.getPwd())) {
                return ResultData.build(500, "原始密码错误");
            }

            if (StringUtils.isEmpty(pwd)) {
                return ResultData.build(500, "原始不能为空");
            }

            employee.setPwd(Tools.encoderByMd5(pwd));
            employeeDao.save(employee);

        } catch (Exception e) {
            return ResultData.build(500, e.getMessage());
        }
        return ResultData.ok();
    }
}
