package com.xuancong.employee_management.repository;

import com.xuancong.employee_management.model.Salary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SalaryRepository extends JpaRepository<Salary,Long> {
    Salary findByActiveIsTrue(boolean active);

    boolean existsByEmployee_Id(Long employeeId);

    Optional<Salary> findByEmployeeIdAndActiveTrue(Long employeeId);

    boolean existsByEmployeeIdAndActiveTrue(Long employeeId);
}
