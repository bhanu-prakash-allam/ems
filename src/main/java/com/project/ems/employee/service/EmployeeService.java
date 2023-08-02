package com.project.ems.employee.service;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.ems.employee.entity.Employee;
import com.project.ems.employee.exception.EmployeeException;
import com.project.ems.employee.repository.EmployeeRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class EmployeeService {
    @Autowired
    private EmployeeRepo employeeRepo;

    ObjectMapper mapper = new ObjectMapper();
    public List<Employee> getAllEmployees()
    {
        List<Employee> list = this.employeeRepo.findAll(Sort.by("name"));
        return list;
    }
    public Employee getEmployee(String name)
    {
        Employee employee = this.employeeRepo.findByName(name);
        if(employee==null)
        {
            throw new RuntimeException("user not found");
        }
        return employee;
    }
    public Employee saveEmployee(Employee employee)
    {

            Employee saved = this.employeeRepo.save(employee);
            return saved;
    }
    public Integer deleteEmployee(String name)
    {
        Integer deleted = this.employeeRepo.deleteByName(name);
        return deleted;
    }
    public Employee patchEmployee(Map<String,Object> fields)
    {

        Employee dbEmployee = this.employeeRepo.findById(Integer.parseInt(fields.get("eid").toString())).get();
        fields.remove("eid");
        fields.forEach((k, v) -> {
            // use reflection to get field k on object and set it to value v
            // Change Claim.class to whatver your object is: Object.class
            Field field = ReflectionUtils.findField(Employee.class, k); // find field in the object class
            field.setAccessible(true);
            ReflectionUtils.setField(field, dbEmployee, v); // set given field for defined object to value V
        });
        this.employeeRepo.save(dbEmployee);
        return dbEmployee;

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
                Integer hours=Integer.parseInt(timesheet.getValue());
                if ((day.equals("SUNDAY") && hours != 0) || (!day.equals("SUNDAY") && hours >= 9)) {
                    timeResponse.put(entry.getKey(), "Reject()");
                    break;
                } else {
                    timeResponse.put(entry.getKey(), "Processed");
                }
            }
        }
        timeResponse.entrySet().stream().forEach(entry->{
            Employee employee = employeeRepo.findByName(entry.getKey());
            employee.setTimesheet(entry.getValue());
            employeeRepo.save(employee);

        });
        return timeResponse;
    }
    public Map<String,String> processTimeSheetVersion2(String message) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, List<Map<String, String>>> inputMap = mapper.readValue(message, Map.class);
        Map<String,String> timeresult=new HashMap<>();
        List<Map<String, String>> timeData = inputMap.get("records");
        timeData.stream().forEach(map->{
            String name = map.get("name");
            boolean allmatch = map.entrySet().stream().skip(1).allMatch((entry) -> {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-MMM-yyyy", Locale.ENGLISH);
                String day = LocalDate.parse(entry.getKey(), formatter).getDayOfWeek().toString();
                Integer hours = Integer.parseInt(entry.getValue());

                return (day.equals("SUNDAY") && hours == 0) || (!day.equals("SUNDAY") && hours < 9);

            });
            timeresult.put(name, allmatch ? "Processed" : "Reject");
        });
        timeresult.entrySet().stream().forEach(entry->{
            Employee employee = employeeRepo.findByName(entry.getKey());
            employee.setTimesheet(entry.getValue());
            employeeRepo.save(employee);

        });
        return timeresult;
    }

}
