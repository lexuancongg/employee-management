package com.xuancong.employee_management.dto.branch;

import com.xuancong.employee_management.dto.address.AddressResponse;
import com.xuancong.employee_management.model.Branch;

public record BranchResponse(
        Long id,
        String name,
        AddressResponse address

) {

    public Branch toBranch() {

    }
}
