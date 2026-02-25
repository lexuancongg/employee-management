package com.xuancong.employee_management.dto.employee;

import com.xuancong.employee_management.dto.department.DepartmentGetResponse;
import com.xuancong.employee_management.dto.image.ImageGetResponse;
import com.xuancong.employee_management.dto.position.PositionGetResponse;
import com.xuancong.employee_management.enums.EmployeeStatus;
import com.xuancong.employee_management.model.Employee;

import java.time.LocalDate;

public record EmployeeDetailGetResponse(
        Long id,
        String name,
        String gender,
        String email,
        String phone,
        LocalDate birthday,
        LocalDate  hireDate,
        EmployeeStatus status,
        PositionGetResponse position,
        DepartmentGetResponse department,
        ImageGetResponse avatar

) {
    public static EmployeeDetailGetResponse from(Employee employee) {
        PositionGetResponse position = PositionGetResponse.fromPosition(employee.getPosition());
        DepartmentGetResponse department = DepartmentGetResponse.fromDepartment(employee.getDepartment());
        return new EmployeeDetailGetResponse(
                employee.getId(),
                employee.getName(),
                employee.getGender(),
                employee.getEmail(),
                employee.getPhone(),
                employee.getBirthday(),
                employee.getHireDate(),
                employee.getStatus(),
                position,
                department,
                null
        );

    }
}
