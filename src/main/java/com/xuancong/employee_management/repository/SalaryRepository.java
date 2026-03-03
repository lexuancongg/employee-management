package com.xuancong.employee_management.repository;

import com.xuancong.employee_management.model.Employee;
import com.xuancong.employee_management.model.Salary;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SalaryRepository extends JpaRepository<Salary,Long> {


    Optional<Salary> findByEmployee_IdAndActiveTrue(Long employeeId);

    boolean existsByEmployee_IdAndActiveTrue(Long employeeId);

    List<Salary> findByEmployee(Employee employee, Sort sort);
}
