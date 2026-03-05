package com.xuancong.employee_management.dto.dashboard;

public record EmployeeStatusResponse(
        long active,
        long onLeave,
        long pending
) {
}
