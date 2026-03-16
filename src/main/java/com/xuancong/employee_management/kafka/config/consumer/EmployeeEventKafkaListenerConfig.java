package com.xuancong.employee_management.kafka.config.consumer;

import com.xuancong.employee_management.kafka.message.EmployCreatedMessage;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
@Configuration
@EnableKafka
public class EmployeeEventKafkaListenerConfig extends BaseKafkaListenerConfig<String, EmployCreatedMessage> {
    public EmployeeEventKafkaListenerConfig(KafkaProperties kafkaProperties) {
        super(String.class, EmployCreatedMessage.class, kafkaProperties);

    }

    @Bean(name = "employeeKafkaListenerContainer")
    @Override
    public ConcurrentKafkaListenerContainerFactory<String, EmployCreatedMessage> kafkaListenerContainerFactory() {
        return super.listenerContainerFactory();
    }
}
