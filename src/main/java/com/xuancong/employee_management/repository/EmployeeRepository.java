package com.xuancong.employee_management.repository;

import com.xuancong.employee_management.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    boolean existsByPosition_Id(Long positionId);
}
