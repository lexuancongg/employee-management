package com.xuancong.employee_management.specification;

import com.xuancong.employee_management.enums.EmployeeStatus;
import com.xuancong.employee_management.model.Employee;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeeSpecification {

    public static Specification<Employee> filter(
            String name,
            String code,
            String email,
            Long departmentId,
            Long branchId,
            Long positionId,
            EmployeeStatus status,
            LocalDate hireDateFrom,
            LocalDate hireDateTo
    ) {
        return (Root<Employee> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (hasText(name)) {
                predicates.add(cb.like(
                        cb.function("unaccent", String.class, cb.lower(root.get("name"))),
                        cb.function("unaccent", String.class, cb.literal("%" + name.toLowerCase() + "%"))
                ));
            }

            if (hasText(code)) {
                predicates.add(cb.like(
                        cb.function("unaccent", String.class, cb.lower(root.get("employeeCode"))),
                        cb.function("unaccent", String.class, cb.literal("%" + code.toLowerCase() + "%"))
                ));
            }

            if (hasText(email)) {
                predicates.add(cb.like(
                        cb.function("unaccent", String.class, cb.lower(root.get("email"))),
                        cb.function("unaccent", String.class, cb.literal("%" + email.toLowerCase() + "%"))
                ));
            }

            if (departmentId != null) {
                predicates.add(cb.equal(root.join("department", JoinType.LEFT).get("id"), departmentId));
            }

            if (branchId != null) {
                predicates.add(cb.equal(
                        root.join("department", JoinType.LEFT).join("branch", JoinType.LEFT).get("id"),
                        branchId
                ));
            }
            if (positionId != null) {
                predicates.add(cb.equal(root.join("position", JoinType.LEFT).get("id"), positionId));
            }

            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            if (hireDateFrom != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("hireDate"), hireDateFrom));
            }
            if (hireDateTo != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("hireDate"), hireDateTo));
            }

            return predicates.isEmpty() ? cb.conjunction() : cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static boolean hasText(String value) {
        return value != null && !value.isBlank();
    }



    public static Specification<Employee> searchByKeyword(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isBlank()) {
                return cb.conjunction(); // luôn true
            }

            String likePattern = "%" + keyword.toLowerCase() + "%";

            return cb.or(
                    cb.like(cb.lower(root.get("name")), likePattern),
                    cb.like(cb.lower(root.get("email")), likePattern)
            );
        };
    }
}