package com.xuancong.employee_management.dto.address;

import com.xuancong.employee_management.model.Address;

public record AddressResponse(
        Long id,
        Long countryId,
        String countryName,
        Long provinceId,
        String provinceName,
        Long districtId,
        String districtName,
        String specificAddress
) {

}
