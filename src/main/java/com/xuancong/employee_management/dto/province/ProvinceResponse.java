package com.xuancong.employee_management.dto.province;

import com.xuancong.employee_management.model.Province;
import lombok.Builder;

@Builder
public record ProvinceResponse(
        Long id,
        String name,
        Long countryId,
        String countryName,
        String type
) {
    public static ProvinceResponse fromProvince(Province province) {
        return ProvinceResponse.builder()
                .id(province.getId())
                .name(province.getName())
                .countryName(province.getCountry().getName())
                .countryId(province.getCountry().getId())
                .build();
    }
}
