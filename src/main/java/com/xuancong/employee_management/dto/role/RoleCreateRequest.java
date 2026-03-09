package com.xuancong.employee_management.dto.role;

import com.xuancong.employee_management.model.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RoleCreateRequest(
        @NotBlank String name
) {
    public Role toRole(){
        return Role.builder()
                .name(name)
                .build();
    }
}
