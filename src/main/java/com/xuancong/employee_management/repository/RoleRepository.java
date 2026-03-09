package com.xuancong.employee_management.repository;

import com.xuancong.employee_management.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);
}
