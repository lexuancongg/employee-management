package com.xuancong.employee_management.event;

public record EmployeeCreatedEvent(
        String email,
        String employeeCode,
        String rawPassword
) {}