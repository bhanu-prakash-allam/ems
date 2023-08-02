package com.project.ems.employee.repository;

import com.project.ems.employee.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee,Integer> {

    public Integer deleteByName(String name);
    public Employee findByName(String name);
}
