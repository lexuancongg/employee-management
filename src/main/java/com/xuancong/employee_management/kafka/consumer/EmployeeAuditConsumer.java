package com.xuancong.employee_management.kafka.consumer;

import com.xuancong.employee_management.event.EmployeeCreatedEvent;
import com.xuancong.employee_management.kafka.message.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmployeeAuditConsumer {


    @KafkaListener(
            topics = "employee-created-topic",
            groupId = "employee-audit-group"
    )
    public void handle(Event<EmployeeCreatedEvent> event) {
    }
}