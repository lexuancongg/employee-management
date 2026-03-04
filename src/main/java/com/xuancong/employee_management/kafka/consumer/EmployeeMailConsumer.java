package com.xuancong.employee_management.kafka.consumer;

import com.xuancong.employee_management.event.EmployeeCreatedEvent;
import com.xuancong.employee_management.kafka.message.Event;
import com.xuancong.employee_management.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmployeeMailConsumer {

    private final MailService mailService;

    @KafkaListener(topics = "employee-created-topic", groupId = "employee-group")
    public void handleEmployeeCreated(Event<EmployeeCreatedEvent> event) {

        mailService.sendAccountEmail(
                event.getData().email(),
                event.getData().employeeCode(),
                event.getData().rawPassword()
        );
    }
}