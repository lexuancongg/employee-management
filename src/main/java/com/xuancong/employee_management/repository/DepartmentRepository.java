package com.xuancong.employee_management.repository;

import com.xuancong.employee_management.model.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);

    Page<Department> findByNameContainingIgnoreCase(String name, Pageable pageable);

    boolean existsByNameIgnoreCaseAndBranch_IdAndIdNot(String name, Long branchId, Long id);

    Page<Department> findByNameContainingIgnoreCaseAndBranch_Id(String name, Long branchId,Pageable pageable);
}
