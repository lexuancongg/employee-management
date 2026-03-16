package com.xuancong.employee_management.kafka.message;

import lombok.Builder;

@Builder
public record EmployCreatedMessage(
        String email,
        String password,
        String username
) {
}
