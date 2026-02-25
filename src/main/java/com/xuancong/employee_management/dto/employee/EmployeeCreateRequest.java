package com.xuancong.employee_management.dto.employee;

import com.xuancong.employee_management.enums.EmployeeStatus;
import com.xuancong.employee_management.model.Employee;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record EmployeeCreateRequest(
        String name,
        String gender,
        @Email(message = "Invalid email format")
        String email,
        @Pattern(
                regexp = "^(0|\\+84)[0-9]{9}$",
                message = "Invalid Vietnamese phone number"
        )
                @NotBlank
        String phone,
        LocalDate birthday,
        LocalDate hireDate,
        EmployeeStatus status,
        Long avatarId,
        Long positionId,
        Long departmentId
) {
        public Employee toBaseEmployee() {
                return Employee.builder()
                        .name(name)
                        .gender(gender)
                        .email(email)
                        .phone(phone)
                        .birthday(birthday)
                        .hireDate(hireDate)
                        .status(status)
                        .avatarId(avatarId)
                        .build();
        }

}
