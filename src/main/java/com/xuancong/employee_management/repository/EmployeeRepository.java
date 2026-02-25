package com.xuancong.employee_management.repository;

import com.xuancong.employee_management.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    boolean existsByPosition_Id(Long positionId);

    @Query("""
        SELECT MAX(e.employeeCode)
        FROM Employee e
    """)
    String findMaxEmployeeCode();

    boolean existsByPhoneAndIdNot(String phone, Long id);
    boolean existsByEmailAndIdNot(String email, Long id);
}
