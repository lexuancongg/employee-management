package com.xuancong.employee_management.dto.province;

import com.xuancong.employee_management.model.Country;
import com.xuancong.employee_management.model.Province;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record ProvinceCreateRequest(
        @NotBlank @Size(min = 1,max = 100) String name,
        @NotNull Long countryId
) {
        public Province toCountry(Country country){
        return Province.builder()
                .name(name)
                .country(country)
                .build();
    }
}
