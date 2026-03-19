package com.xuancong.employee_management.dto.department;

import com.xuancong.employee_management.model.Department;

public record DepartmentResponse(
        Long id,
        String name,
        String branchName,
        Long branchId
) {
    public static DepartmentResponse fromDepartment(Department department) {
        return new DepartmentResponse(
                department.getId(),
                department.getName(),
                department.getBranch().getName(),
                department.getBranch().getId()
        );
    }
}
