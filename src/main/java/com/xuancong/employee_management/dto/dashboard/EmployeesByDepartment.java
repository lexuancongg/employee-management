package com.xuancong.employee_management.dto.dashboard;

public record EmployeesByDepartment(
        String departmentName,
        long employeeCount
) {}
