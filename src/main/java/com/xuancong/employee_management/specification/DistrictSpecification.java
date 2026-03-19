package com.xuancong.employee_management.specification;

import com.xuancong.employee_management.model.District;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class DistrictSpecification {

    public static Specification<District> getDistrictByFilter(
            String name,
            Long countryId,
            Long provinceId
    ) {
        return (root, query, cb) -> {
            var predicates = new java.util.ArrayList<Predicate>();

            if (name != null && !name.isEmpty()) {
                predicates.add(
                        cb.like(
                                cb.function(
                                        "unaccent",
                                        String.class,
                                        cb.lower(root.get("name"))
                                ),
                                cb.function(
                                        "unaccent",
                                        String.class,
                                        cb.literal("%" + name.toLowerCase() + "%")
                                )
                        )
                );
            }

            Join<Object, Object> provinceJoin = null;

            if (provinceId != null || countryId != null) {
                provinceJoin = root.join("province");
            }

            if (provinceId != null) {
                predicates.add(
                        cb.equal(provinceJoin.get("id"), provinceId)
                );
            }

            if (countryId != null) {
                Join<Object, Object> countryJoin = provinceJoin.join("country");

                predicates.add(
                        cb.equal(countryJoin.get("id"), countryId)
                );
            }

            if (predicates.isEmpty()) {
                return cb.conjunction();
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}