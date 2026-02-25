package com.xuancong.employee_management.specification;

import com.xuancong.employee_management.model.Employee;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class EmployeeSpecification {
    public static Specification<Employee> filter(
            String name,
            String code,
            String email,
            Long departmentId
    ) {
        return  new Specification<Employee>() {

            @Override
            public Predicate toPredicate(Root<Employee> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (hasText(name)) {
                    predicates.add(
                            criteriaBuilder.like(
                                    criteriaBuilder.lower(root.get("name")),
                                    likePattern(name)
                            )
                    );
                }
                if (hasText(code)) {
                    predicates.add(
                            criteriaBuilder.like(
                                    criteriaBuilder.lower(root.get("employeeCode")),
                                    likePattern(code)
                            )
                    );
                }
                if (hasText(email)) {
                    predicates.add(
                            criteriaBuilder.like(
                                    criteriaBuilder.lower(root.get("email")),
                                    likePattern(email)
                            )
                    );
                }

                if (departmentId != null) {
                    predicates.add(
                            criteriaBuilder.equal(
                                    root.join("department").get("id"),
                                    departmentId
                            )
                    );
                }

                return predicates.isEmpty()
                        ? criteriaBuilder.conjunction()
                        : criteriaBuilder.and(predicates.toArray(new Predicate[0]));

            }
        };
    }
    private static boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    private static String likePattern(String value) {
        return "%" + value.toLowerCase().trim() + "%";
    }

}
