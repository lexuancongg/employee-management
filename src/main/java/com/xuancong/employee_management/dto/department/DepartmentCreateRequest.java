package com.xuancong.employee_management.dto.department;

import com.xuancong.employee_management.model.Branch;
import com.xuancong.employee_management.model.Department;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DepartmentCreateRequest(
        @NotBlank(message = "name must be not blank") String name,
        @NotNull Long branchId
) {
    public Department toDepartment(Branch branch) {
        return Department.builder()
                .name(this.name)
                .branch(branch)
                .build();
    }
}
