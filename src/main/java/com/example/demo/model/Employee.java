package com.example.demo.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "emp")
public class Employee {

    /**
     * 员工编号
     */
    @Id
    @GeneratedValue
    @Column(name = "e_id")
    private int id;

    /**
     * 员工头像
     */
    @Column(name = "e_portrait")
    private String portrait;

    /**
     * 员工账号
     */
    @Column(name = "e_name")
    private String name;

    /**
     * 员工密码
     */
    @Column(name = "e_pwd")
    private String pwd;

    /**
     * 员工姓名
     */
    @Column(name = "e_uname")
    private String uname;

    /**
     * 员工生日
     */
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Column(name = "e_birthday")
    private Date birthday;

    /**
     * 员工级别
     */
    @Column(name = "e_level")
    private int level;

    /**
     * 员工性别
     */
    @Column(name = "e_six")
    private int six;

    /**
     * 员工工资
     */
    @Column(name = "e_wages")
    private long wages;

    /**
     * 员工爱好
     */
    @Column(name = "e_hobby")
    private String hobby;

    /**
     * 部门
     */
    @ManyToOne
    @JoinColumn(name = "e_deptid")
    private Department department;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getSix() {
        return six;
    }

    public void setSix(int six) {
        this.six = six;
    }

    public long getWages() {
        return wages;
    }

    public void setWages(long wages) {
        this.wages = wages;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
}
