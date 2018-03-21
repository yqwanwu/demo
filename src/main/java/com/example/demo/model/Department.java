package com.example.demo.model;

import javax.persistence.*;

@Entity
@Table(name = "dept")
public class Department {
    /**
     * 部门编号
     */
    @Id
    @GeneratedValue
    @Column(name = "d_id")
    private int id;

    /**
     * 部门名称
     */
    @Column(name = "d_name")
    private String name;

    /**
     * 部门地点
     */
    @Column(name = "d_location")
    private String location;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
