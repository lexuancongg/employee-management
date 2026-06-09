package com.xuancong.employee_management.repository;

import com.xuancong.employee_management.model.JobTitle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface JobTitleRepository extends JpaRepository<JobTitle, Long> , JpaSpecificationExecutor<JobTitle> {
}
