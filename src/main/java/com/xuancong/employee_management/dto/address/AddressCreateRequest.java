package com.xuancong.employee_management.dto.address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddressCreateRequest(
        @NotNull
        Long countryId,
        @NotNull
        Long provinceId,
        @NotNull
        Long districtId,
        @NotBlank
        String specificAddress
) {

}
