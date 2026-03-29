package com.xuancong.employee_management.specification;

import com.xuancong.employee_management.model.District;
import com.xuancong.employee_management.model.Province;
import com.xuancong.employee_management.model.Country;
import jakarta.persistence.criteria.Fetch;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
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

            Join<District, Province> provinceJoin = null;

            if (query.getResultType() != Long.class) {
                Fetch<District, Province> provinceFetch = root.fetch("province", JoinType.LEFT);
                provinceJoin = (Join<District, Province>) provinceFetch;
                provinceJoin.fetch("country", JoinType.LEFT);
                query.distinct(true);
            }

            if (name != null && !name.isEmpty()) {
                predicates.add(cb.like(
                        cb.function("unaccent", String.class, cb.lower(root.get("name"))),
                        cb.function("unaccent", String.class, cb.literal("%" + name.toLowerCase() + "%"))
                ));
            }

            if (provinceId != null) {
                if (provinceJoin == null) {
                    provinceJoin = root.join("province", JoinType.LEFT);
                    provinceJoin.fetch("country", JoinType.LEFT);
                }
                predicates.add(cb.equal(provinceJoin.get("id"), provinceId));
            }

            if (countryId != null) {
                if (provinceJoin == null) {
                    provinceJoin = root.join("province", JoinType.LEFT);
                }
                Join<Province, Country> countryJoin = provinceJoin.join("country", JoinType.LEFT);
                predicates.add(cb.equal(countryJoin.get("id"), countryId));
            }

            if (predicates.isEmpty()) {
                return cb.conjunction();
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}