package com.xuancong.employee_management.specification;

import com.xuancong.employee_management.model.Salary;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class SalarySpecification {

    public static Specification<Salary> filter(
            String keyword,
            String email,
            Long branchId,
            Long departmentId,
            Long positionId
    ) {
        return (Root<Salary> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            Join<Object, Object> employeeJoin = root.join("employee", JoinType.LEFT);

            // Keyword filter (tên nhân viên)
            if (hasText(keyword)) {
                predicates.add(cb.like(
                        cb.function("unaccent", String.class, cb.lower(employeeJoin.get("name"))),
                        cb.function("unaccent", String.class, cb.literal("%" + keyword.toLowerCase() + "%"))
                ));
            }

            if (hasText(email)) {
                predicates.add(cb.like(
                        cb.function("unaccent", String.class, cb.lower(employeeJoin.get("email"))),
                        cb.function("unaccent", String.class, cb.literal("%" + email.toLowerCase() + "%"))
                ));
            }

            if (departmentId != null) {
                predicates.add(cb.equal(employeeJoin.join("department", JoinType.LEFT).get("id"), departmentId));
            }

            if (branchId != null) {
                predicates.add(cb.equal(
                        employeeJoin.join("department", JoinType.LEFT).join("branch", JoinType.LEFT).get("id"),
                        branchId
                ));
            }

            if (positionId != null) {
                predicates.add(cb.equal(employeeJoin.join("position", JoinType.LEFT).get("id"), positionId));
            }



            return predicates.isEmpty() ? cb.conjunction() : cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}