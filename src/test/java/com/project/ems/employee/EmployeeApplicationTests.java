package com.project.ems.employee;

import com.project.ems.employee.configuration.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringBootTest
@SpringJUnitConfig(TestConfig.class)
class EmployeeApplicationTests {

	@Test
	void contextLoads()  {

	}

}
