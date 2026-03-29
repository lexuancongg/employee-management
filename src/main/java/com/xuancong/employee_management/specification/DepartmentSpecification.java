package com.xuancong.employee_management.specification;

import com.xuancong.employee_management.model.Department;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class DepartmentSpecification {

    public static Specification<Department> getDepartmentFilter(
            String name,
            Long branchId
    ) {
        return (root, query, cb) -> {
            var predicates = new java.util.ArrayList<Predicate>();

            assert query != null;
            if (query.getResultType() != Long.class) {
                root.fetch("branch", JoinType.LEFT);
                query.distinct(true);
            }

            if (name != null && !name.isEmpty()) {
                predicates.add(
                        cb.like(
                                cb.function("unaccent", String.class, cb.lower(root.get("name"))),
                                cb.function("unaccent", String.class, cb.literal("%" + name.toLowerCase() + "%"))
                        )
                );
            }

            if (branchId != null) {
                Join<Object, Object> branchJoin = root.join("branch", JoinType.LEFT);
                predicates.add(cb.equal(branchJoin.get("id"), branchId));
            }

            if (predicates.isEmpty()) {
                return cb.conjunction();
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}