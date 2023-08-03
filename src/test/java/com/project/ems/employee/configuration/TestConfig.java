package com.project.ems.employee.configuration;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.client.RestTemplate;

@TestConfiguration
@EnableAutoConfiguration(exclude = {KafkaAutoConfiguration. class})
public class TestConfig {
    @MockBean
    KafkaTemplate kafkaTemplate;

}
