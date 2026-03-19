package com.xuancong.employee_management.dto.district;

import lombok.Builder;

@Builder
public record DistrictCreateRequest(
        String name,
        Long provinceId
) {}