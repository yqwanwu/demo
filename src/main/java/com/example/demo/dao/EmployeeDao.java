package com.example.demo.dao;

import com.example.demo.model.Employee;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeDao extends CrudRepository<Employee, Integer> {
    Employee findOneByNameAndAndPwd(String name, String pwd);

    Employee findOneByName(String name);
}
