package com.xuancong.employee_management.repository;

import com.xuancong.employee_management.model.Employee;
import com.xuancong.employee_management.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> , JpaSpecificationExecutor<Employee> {
    boolean existsByPosition_Id(Long positionId);

    @Query("""
        SELECT MAX(e.employeeCode)
        FROM Employee e
    """)
    String findMaxEmployeeCode();

    boolean existsByPhoneAndIdNot(String phone, Long id);
    boolean existsByEmailAndIdNot(String email, Long id);

    boolean existsByDepartment_Id(Long departmentId);

    Optional<Employee> findByUser_Username(String userUsername);

    String user(User user);
}
