package com.xuancong.employee_management.repository;

import com.xuancong.employee_management.model.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

}
