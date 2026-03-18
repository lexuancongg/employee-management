package com.xuancong.employee_management.specification;

import com.xuancong.employee_management.model.Province;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class ProvinceSpecification {

    public static Specification<Province> getProvincesByCountryId(Long countryId, String name) {
        return (root, query, criteriaBuilder) -> {
            if (countryId == null && (name == null || name.isEmpty())) {
                return criteriaBuilder.conjunction();
            }

            var predicates = new java.util.ArrayList<Predicate>();

            if (countryId != null) {
                predicates.add(criteriaBuilder.equal(root.get("country").get("id"), countryId));
            }

            if (name != null && !name.isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(root.get("name"))),
                        "%" + name.toLowerCase() + "%"
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}