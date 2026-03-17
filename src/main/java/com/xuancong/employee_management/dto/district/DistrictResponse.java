package com.xuancong.employee_management.dto.district;

import com.xuancong.employee_management.model.District;
import lombok.Builder;

@Builder
public record DistrictResponse(Long id, String name) {
    public static DistrictResponse fromDistrict(District district) {
        return DistrictResponse.builder()
                .id(district.getId())
                .name(district.getName())
                .build();
    }
}
