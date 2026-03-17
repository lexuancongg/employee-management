package com.xuancong.employee_management.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class TopicConfig {
    @Bean
    public NewTopic employeeCreatedTopic() {
        return TopicBuilder.name("employee-created-topic")
                .partitions(3)
                .replicas(1)
                .build();
    }
}
