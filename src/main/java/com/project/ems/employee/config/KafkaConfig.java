package com.project.ems.employee.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {
    @Value("${kafka.topic.name}")
    private String topic;
    @Bean
    public NewTopic employeeTopic(){
        return TopicBuilder.name(topic)
                .partitions(2)
                .build();
    }
}
