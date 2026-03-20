package com.xuancong.employee_management.dto.employee;

import com.xuancong.employee_management.dto.address.AddressResponse;
import com.xuancong.employee_management.dto.department.DepartmentResponse;
import com.xuancong.employee_management.dto.image.ImageGetResponse;
import com.xuancong.employee_management.dto.position.PositionResponse;
import com.xuancong.employee_management.enums.EmployeeStatus;
import com.xuancong.employee_management.model.Employee;

import java.time.LocalDate;

public record EmployeeDetailResponse(
        Long id,
        String name,
        String gender,
        String email,
        String phone,
        LocalDate birthday,
        LocalDate  hireDate,
        EmployeeStatus status,
        PositionResponse position,
        DepartmentResponse department,
        ImageGetResponse avatar,
        AddressResponse address

) {
    public static EmployeeDetailResponse from(Employee employee) {
        PositionResponse position = PositionResponse.fromPosition(employee.getPosition());
        DepartmentResponse department = DepartmentResponse.fromDepartment(employee.getDepartment());
//        AddressResponse address = AddressResponse.fromAddress(employee.getAddress());
        return new EmployeeDetailResponse(
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
                null,
                null

        );

    }
}
