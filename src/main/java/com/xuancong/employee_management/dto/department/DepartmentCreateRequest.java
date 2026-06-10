package com.xuancong.employee_management.dto.department;

import com.xuancong.employee_management.enums.DepartmentStatus;
import com.xuancong.employee_management.model.Branch;
import com.xuancong.employee_management.model.Department;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DepartmentCreateRequest(
        @NotBlank(message = "name must be not blank") String name,
        @NotNull Long branchId,
        Long managerId,
        String departmentCode,
        DepartmentStatus status

) {
    public Department toDepartment(Branch branch) {
        return Department.builder()
                .name(this.name)
                .status(this.status)
                .departmentCode(this.departmentCode)
                .branch(branch)
                .build();
    }
}
