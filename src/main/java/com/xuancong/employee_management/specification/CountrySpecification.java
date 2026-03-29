package com.xuancong.employee_management.specification;

import com.xuancong.employee_management.model.Country;
import org.apache.commons.lang3.StringUtils;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class CountrySpecification {
    public static Specification<Country> filterCountryByName(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            var predicates = new java.util.ArrayList<Predicate>();

            // Bỏ dấu + lowercase input ở Java
            String pattern = StringUtils.stripAccents(name).toLowerCase();

            predicates.add(criteriaBuilder.like(
                    criteriaBuilder.function("unaccent", String.class,
                            criteriaBuilder.lower(root.get("name"))),
                    "%" + pattern + "%"
            ));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}