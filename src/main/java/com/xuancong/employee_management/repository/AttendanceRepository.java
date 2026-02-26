package com.xuancong.employee_management.repository;

import com.xuancong.employee_management.model.Attendance;
import com.xuancong.employee_management.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    boolean existsByEmployeeAndWorkDate(Employee employee, LocalDate workDate);
}
