package com.example.demo.dao;

import com.example.demo.model.Employee;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeDao extends PagingAndSortingRepository<Employee, Integer>, JpaSpecificationExecutor<Employee> {
    Employee findOneByNameAndAndPwd(String name, String pwd);

    Employee findOneByName(String name);

    @Query("SELECT hobby from Employee GROUP BY hobby ")
    List<String> findAllHoddy();
}
