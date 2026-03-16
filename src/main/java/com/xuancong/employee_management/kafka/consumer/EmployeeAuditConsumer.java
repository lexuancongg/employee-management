package com.xuancong.employee_management.kafka.consumer;

import com.xuancong.employee_management.kafka.message.EmployCreatedMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmployeeAuditConsumer {


    @KafkaListener(
            topics = "employee-created-topic",
            groupId = "employee-audit-group",
            containerFactory = "employeeKafkaListenerContainer"
    )
    public void handle(EmployCreatedMessage message,
                       @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                       @Header(KafkaHeaders.OFFSET) long offset) {
        System.out.println("Partition: " + partition + ", Offset: " + offset);
        System.out.println("Message: " + message);
    }
}