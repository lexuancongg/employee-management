package com.xuancong.employee_management.dto.department;

import com.xuancong.employee_management.model.Department;

public record DepartmentGetResponse(
        Long id,
        String name
) {
    public static DepartmentGetResponse fromDepartment(Department department) {
        return new DepartmentGetResponse(department.getId(), department.getName());
    }
}
