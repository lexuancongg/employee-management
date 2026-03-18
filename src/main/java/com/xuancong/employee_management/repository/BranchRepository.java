package com.xuancong.employee_management.repository;

import com.xuancong.employee_management.model.Branch;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BranchRepository extends JpaRepository<Branch, Long> , JpaSpecificationExecutor<Branch> {
}
