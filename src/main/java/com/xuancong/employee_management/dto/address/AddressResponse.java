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
    public static AddressResponse fromAddress(Address address) {
        return new AddressResponse(
                address.getId(),
                address.getCountry().getId(),
                address.getCountry().getName(),
                address.getProvince().getId(),
                address.getProvince().getName(),
                address.getDistrict().getId(),
                address.getDistrict().getName(),
                address.getSpecificAddress()
        );
    }

}
