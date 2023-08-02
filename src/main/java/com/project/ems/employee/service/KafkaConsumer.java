package com.project.ems.employee.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class KafkaConsumer {

    @Autowired
   private EmployeeService employeeService;
    @KafkaListener(topics = "${kafka.topic.name}",
            groupId = "{spring.kafka.consumer.group-id}")
    public void consume(String message, @Header(KafkaHeaders.RECEIVED_PARTITION) String partition,@Header(KafkaHeaders.OFFSET) Long offset) throws JsonProcessingException {
        log.info("Message received from partition {} and offset {} {} ",partition,offset,message);
        Map<String, String> timeResponse = employeeService.processTimeSheet(message);
        log.info(String.format("Timesheet Data -> %s", timeResponse));
    }

}
