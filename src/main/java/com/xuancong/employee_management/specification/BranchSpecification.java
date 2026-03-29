package com.xuancong.employee_management.specification;

import com.xuancong.employee_management.model.Branch;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class BranchSpecification {

    public static Specification<Branch> getBranchFilter(String name, Long countryId, Long provinceId) {
        return (root, query, criteriaBuilder) -> {
            var predicates = new java.util.ArrayList<Predicate>();

            if (name != null && !name.isEmpty()) {
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.function(
                                        "unaccent", String.class,
                                        criteriaBuilder.lower(root.get("name"))
                                ),
                                criteriaBuilder.function(
                                        "unaccent", String.class,
                                        criteriaBuilder.literal("%" + name.toLowerCase() + "%")
                                )
                        )
                );
            }

            var addressJoin = root.join("address");

            if (countryId != null) {
                predicates.add(
                        criteriaBuilder.equal(addressJoin.get("country").get("id"), countryId)
                );
            }

            if (provinceId != null) {
                predicates.add(
                        criteriaBuilder.equal(addressJoin.get("province").get("id"), provinceId)
                );
            }

            if (predicates.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}