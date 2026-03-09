package com.xuancong.employee_management.dto.branch;

import com.xuancong.employee_management.dto.address.AddressCreateRequest;
import jakarta.validation.constraints.NotBlank;

public record BranchCreateRequest(
        @NotBlank
        String name,
        AddressCreateRequest address
) {
}
