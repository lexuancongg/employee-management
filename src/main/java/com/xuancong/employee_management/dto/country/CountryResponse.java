package com.xuancong.employee_management.dto.country;


import com.xuancong.employee_management.model.Country;

public record CountryResponse(Long id, String name) {
    public static CountryResponse fromCountry(Country country) {
        return new CountryResponse(country.getId(), country.getName());
    }

}
