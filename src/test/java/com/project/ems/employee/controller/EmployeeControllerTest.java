package com.project.ems.employee.controller;

import com.project.ems.employee.entity.Employee;
import com.project.ems.employee.service.EmployeeService;
import com.project.ems.employee.service.KafkaProducer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
@ExtendWith(MockitoExtension.class)
public class EmployeeControllerTest {
    @Mock
    private EmployeeService employeeService;
    @Mock
    private KafkaProducer kafkaProducer;
    @InjectMocks
    EmployeeController employeeController;

    @Test
    public void testGetAllEmployees()
    {
        Mockito.when(employeeService.getAllEmployees()).thenReturn(getEmployee());
        ResponseEntity<List<Employee>> response = employeeController.getAllEmployees();
        Assertions.assertEquals("bhanu",response.getBody().get(0).getName());
    }
    @Test
    public void testgetEmployee()
    {
        Mockito.when(employeeService.getEmployee("name")).thenReturn(getEmployee().get(0));
        ResponseEntity<Employee> response = employeeController.getEmployee("name");
        Assertions.assertEquals("bhanu",response.getBody().getName());
    }
    @Test
    public void testSaveEmployee()
    {
        Mockito.when(employeeService.saveEmployee(Mockito.any())).thenReturn(getEmployee().get(0));
        ResponseEntity<Employee> response = employeeController.saveEmployee(getEmployee().get(0));
        Assertions.assertEquals("bhanu",response.getBody().getName());
    }
    public List<Employee> getEmployee()
    {
        Employee employee=new Employee();
        employee.setEid(1);
        employee.setName("bhanu");
        employee.setSalary(2000.0f);
        return Arrays.asList(employee);
    }

}
