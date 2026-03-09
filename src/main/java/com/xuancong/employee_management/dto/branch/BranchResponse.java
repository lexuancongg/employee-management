package com.xuancong.employee_management.dto.branch;

import com.xuancong.employee_management.dto.address.AddressResponse;
import com.xuancong.employee_management.model.Branch;

public record BranchResponse(
        Long id,
        String name,
        AddressResponse address

) {

   public static BranchResponse fromBranch(Branch branch) {
       AddressResponse address = AddressResponse.fromAddress(branch.getAddress());
       return new BranchResponse(
               branch.getId(),
               branch.getName(),
               address
       );

   }
}
