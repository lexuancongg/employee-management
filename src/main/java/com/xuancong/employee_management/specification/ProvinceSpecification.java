package com.xuancong.employee_management.specification;

import com.xuancong.employee_management.model.Province;
import org.springframework.data.jpa.domain.Specification;

public class ProvinceSpecification {

    public static Specification<Province> getProvincesByCountryId(Long countryId) {
        return ((root, query, criteriaBuilder) -> {
            if (countryId == null){
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("country").get("id"), countryId);

        });
    }
}
