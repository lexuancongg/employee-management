package com.xuancong.employee_management.repository;

import com.xuancong.employee_management.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
