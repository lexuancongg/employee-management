package com.xuancong.employee_management.kafka.consumer;

import com.xuancong.employee_management.kafka.message.EmployCreatedMessage;
import com.xuancong.employee_management.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmployeeMailConsumer {

    private final MailService mailService;

    @KafkaListener(
            topics = "employee-created-topic",
            groupId = "employee-group",
            containerFactory = "employeeKafkaListenerContainer"
    )
    public void handleEmployeeCreated(EmployCreatedMessage message) {
        System.out.println("Employee Created");

    }
}

