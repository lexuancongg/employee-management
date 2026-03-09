package com.xuancong.employee_management.dto.role;

import com.xuancong.employee_management.model.Role;

public record RoleResponse(
        Long id,
        String name
) {
    public static RoleResponse fromRole(Role role) {
        return new RoleResponse(
                role.getId(),
                role.getName()
        );
    }
}
