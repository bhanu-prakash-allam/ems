package com.project.ems.employee.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.ems.employee.entity.Employee;
import com.project.ems.employee.exception.EmployeeNotFoundException;
import com.project.ems.employee.repository.EmployeeRepo;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Stream;

@Service
@Transactional
@Slf4j
public class EmployeeService {
    @Autowired
    private EmployeeRepo employeeRepo;

    ObjectMapper mapper = new ObjectMapper();
    public List<Employee> getAllEmployees()
    {
        return this.employeeRepo.findAll(Sort.by("name"));
    }
    public Employee getEmployee(String name)
    {
        Employee employee = this.employeeRepo.findByName(name);
        if(employee==null)
        {
            throw new EmployeeNotFoundException("user not found");
        }
        return employee;
    }
    public Employee saveEmployee(Employee employee)
    {
        return this.employeeRepo.save(employee);
    }
    public Integer deleteEmployee(String name)
    {
        return this.employeeRepo.deleteByName(name);
    }

    public void isValid(String message) throws JsonProcessingException {
            mapper.readTree(message);
    }
    public Map<String,String> processTimeSheet(String message) throws JsonProcessingException {
        ObjectMapper mapper=new ObjectMapper();
        Map<String,Map<String,String>> inputMap = mapper.readValue(message, Map.class);
        Map<String,String> timeResponse=new HashMap<>();
        for(Map.Entry<String, Map<String, String>> entry:inputMap.entrySet())
        {

            for(Map.Entry<String, String> timesheet:entry.getValue().entrySet())
            {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-MMM-yyyy", Locale.ENGLISH);
                String day = LocalDate.parse(timesheet.getKey(), formatter).getDayOfWeek().toString();
                int hours=Integer.parseInt(timesheet.getValue());
                if ((day.equals("SUNDAY") && hours != 0) || (!day.equals("SUNDAY") && hours >= 9)) {
                    timeResponse.put(entry.getKey(), "Reject(Invalid hours)");
                    break;
                } else {
                    timeResponse.put(entry.getKey(), "Processed");
                }
            }
        }
        timeResponse.entrySet().stream().forEach(entry->{

            Employee employee = employeeRepo.findByName(entry.getKey());
            if(employee!=null)
            {
                employee.setTimesheet(entry.getValue());
                employeeRepo.save(employee);
            }
        });
        return timeResponse;
    }
//    public Map<String,String> processTimeSheetVersion2(String message) throws JsonProcessingException {
//        ObjectMapper mapper = new ObjectMapper();
//        Map<String, List<Map<String, String>>> inputMap = mapper.readValue(message, Map.class);
//        Map<String,String> timeresult=new HashMap<>();
//        List<Map<String, String>> timeData = inputMap.get("records");
//        timeData.forEach(map->{
//            String name = map.get("name");
//            boolean allmatch = map.entrySet().stream().skip(1).allMatch(entry -> {
//                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-MMM-yyyy", Locale.ENGLISH);
//                String day = LocalDate.parse(entry.getKey(), formatter).getDayOfWeek().toString();
//                int hours = Integer.parseInt(entry.getValue());
//
//                return (day.equals("SUNDAY") && hours == 0) || (!day.equals("SUNDAY") && hours < 9);
//
//            });
//            timeresult.put(name, allmatch ? "Processed" : "Reject");
//        });
//        timeresult.forEach((key, value) -> {
//            Employee employee = employeeRepo.findByName(key);
//            employee.setTimesheet(value);
//            employeeRepo.save(employee);
//
//        });
//        return timeresult;
//    }

}
