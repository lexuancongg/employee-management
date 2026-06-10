package com.xuancong.employee_management.dto.department;

import com.xuancong.employee_management.enums.DepartmentStatus;
import com.xuancong.employee_management.model.Department;

public record DepartmentResponse(
        Long id,
        String name,
        String branchName,
        Long branchId,
        int quantityEmployee,
        String departmentCode,
        DepartmentStatus status,
        String managerName
) {
    public static DepartmentResponse fromDepartment(Department department) {
        return new DepartmentResponse(
                department.getId(),
                department.getName(),
                department.getBranch().getName(),
                department.getBranch().getId(),
                department.getEmployees().size(),
                department.getDepartmentCode(),
                department.getStatus(),
                department.getManager()!=null ? department.getManager().getName() : ""

        );
    }
}
