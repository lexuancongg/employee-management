package com.xuancong.employee_management.dto.department;

import com.xuancong.employee_management.model.Department;
import jakarta.validation.constraints.NotBlank;

public record DepartmentCreateRequest(
        @NotBlank(message = "name must be not blank") String name
) {
    public Department toDepartment() {
        return Department.builder()
                .name(this.name)
                .build();
    }
}
