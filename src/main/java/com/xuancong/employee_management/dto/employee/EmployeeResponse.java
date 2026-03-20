package com.xuancong.employee_management.dto.employee;

import com.xuancong.employee_management.enums.EmployeeStatus;
import com.xuancong.employee_management.model.Employee;

import java.time.LocalDate;

public record EmployeeResponse(
        Long id,
        String employeeCode,
        String name,
        EmployeeStatus status,
        String positionName,
        Long positionId,
        Long departmentId,
        String departmentName,
        Long branchId
) {
    public static EmployeeResponse fromEmployee(Employee employee) {
        return new EmployeeResponse(
                employee.getId(),
                employee.getEmployeeCode(),
                employee.getName(),
                employee.getStatus(),
                employee.getPosition().getName(),
                employee.getPosition().getId(),
                employee.getDepartment().getId(),
                employee.getDepartment().getName(),
                employee.getDepartment().getBranch().getId()

        );
    }

}
