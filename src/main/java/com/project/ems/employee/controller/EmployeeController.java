package com.project.ems.employee.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.ems.employee.entity.Employee;
import com.project.ems.employee.service.EmployeeService;
import com.project.ems.employee.service.KafkaProducer;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private KafkaProducer kafkaProducer;

    @GetMapping(path="/getEmployees")
    public ResponseEntity<List<Employee>> getAllEmployees()
    {
        return new ResponseEntity<List<Employee>>(this.employeeService.getAllEmployees(), HttpStatus.OK);
    }
    @GetMapping(path = "/getEmployee/{name}")
    public ResponseEntity<Employee> getEmployee(@PathVariable String name)
    {
        return new ResponseEntity<Employee>(this.employeeService.getEmployee(name), HttpStatus.OK);
    }
    @PostMapping(path="/saveEmployee")
    public ResponseEntity<Employee> saveEmployee(@Valid @RequestBody Employee employee)
    {
        return new ResponseEntity<Employee>(this.employeeService.saveEmployee(employee), HttpStatus.CREATED);
    }
    @PutMapping(path = "/updateEmployee")
    public ResponseEntity<Employee> updateEmployee(@RequestBody Employee employee)
    {
        return new ResponseEntity<Employee>(this.employeeService.saveEmployee(employee), HttpStatus.CREATED);
    }
    @DeleteMapping(path = "/deleteEmployee/{name}")
    public ResponseEntity<Integer> deleteEmployee(@PathVariable String name)
    {
        return new ResponseEntity<Integer>(this.employeeService.deleteEmployee(name), HttpStatus.NO_CONTENT);
    }
    @PatchMapping(path = "/editEmployee")
    public ResponseEntity<Employee> editEmployee(@RequestBody Map<String,Object> fields)
    {
        return new ResponseEntity<Employee>(this.employeeService.patchEmployee(fields), HttpStatus.CREATED);
    }
    @PatchMapping(path = "/publish")
    public String sendMessagetoKafka(@RequestBody String message) throws JsonProcessingException {
        this.employeeService.isValid(message);
        this.kafkaProducer.sendMessage(message);
        return "Message sent to kafka";
    }
}
