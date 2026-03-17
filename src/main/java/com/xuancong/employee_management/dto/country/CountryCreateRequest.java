package com.xuancong.employee_management.dto.country;

import com.xuancong.employee_management.model.Country;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CountryCreateRequest(
        @NotBlank @Size(min = 1, max = 100) String name
) {
    public Country toCountry(){
        return Country.builder()
                .name(this.name)
                .build();
    }
}
