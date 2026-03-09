package com.xuancong.employee_management.dto.employee;

import com.xuancong.employee_management.enums.EmployeeStatus;
import com.xuancong.employee_management.model.Employee;

import java.time.LocalDate;

public record EmployeeResponse(
        Long id,
        String employeeCode,
        String name,
        String gender,
        String email,
        String phone,
        EmployeeStatus status,
        LocalDate hireDate
) {
    public static EmployeeResponse fromEmployee(Employee employee) {
        return new EmployeeResponse(
                employee.getId(),
                employee.getEmployeeCode(),
                employee.getName(),
                employee.getGender(),
                employee.getEmail(),
                employee.getPhone(),
                employee.getStatus(),
                employee.getHireDate()
        );
    }

}
