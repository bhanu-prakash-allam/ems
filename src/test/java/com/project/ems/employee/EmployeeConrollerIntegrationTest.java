package com.project.ems.employee;

import com.project.ems.employee.configuration.TestConfig;
import com.project.ems.employee.entity.Employee;
import com.project.ems.employee.service.EmployeeService;
import com.project.ems.employee.service.KafkaProducer;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SpringJUnitConfig(TestConfig.class)
@Slf4j
public class EmployeeConrollerIntegrationTest {


    @Autowired
    private EmployeeService employeeService;
    @MockBean
    private KafkaProducer kafkaProducer;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    String baseurl = "http://localhost:";

    @Test
    public void getAllEmployeesTest() throws Exception {
        ResponseEntity<Employee[]> response = restTemplate.getForEntity(baseurl +port+"/getEmployees", Employee[].class);
        Assertions.assertEquals(3,response.getBody().length);

    }
    @Test
    public void getEmployeeTest() throws Exception {
        ResponseEntity<Employee> response = restTemplate.getForEntity(baseurl +port+"/getEmployee/John Doe", Employee.class);
        Assertions.assertEquals("John Doe",response.getBody().getName());

    }
    @Test
    public void saveEmployeeTest() throws Exception {
        Employee employee=new Employee();
        employee.setName("bhanu");
        ResponseEntity<Employee> response = restTemplate.postForEntity(baseurl +port+"/saveEmployee",employee,Employee.class);
        Assertions.assertEquals("bhanu",response.getBody().getName());

    }
    @Test
    public void deleteEmployeeTest() throws Exception {
        Employee employee=new Employee();
        employee.setName("bhanu");
        restTemplate.delete(baseurl +port+"/deleteEmployee/John Doe");
    }
    @Test
    public void updateEmployeeTest() throws Exception {
        Employee employee=new Employee();
        employee.setEid(20);
        employee.setName("vamsi");
        ResponseEntity<Employee> response = restTemplate.exchange(baseurl + port + "/updateEmployee", HttpMethod.PUT, new HttpEntity<>(employee), Employee.class);
        Assertions.assertEquals("vamsi",response.getBody().getName());
    }

}
