package com.xuancong.employee_management.dto.district;

import com.xuancong.employee_management.model.District;
import lombok.Builder;

@Builder
public record DistrictResponse(Long id,
                               String name,
                               Long provinceId,
                               String provinceName,
                               Long countryId,
                               String countryName
) {
    public static DistrictResponse fromDistrict(District district) {
        return DistrictResponse.builder()
                .id(district.getId())
                .name(district.getName())
                .provinceId(district.getProvince().getId())
                .provinceName(district.getProvince().getName())
                .countryId(district.getProvince().getCountry().getId())
                .countryName(district.getProvince().getCountry().getName())
                .build();
    }
}
