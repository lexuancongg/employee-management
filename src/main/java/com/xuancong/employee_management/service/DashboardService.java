package com.xuancong.employee_management.service;

import com.xuancong.employee_management.dto.dashboard.*;
import com.xuancong.employee_management.enums.EmployeeStatus;
import com.xuancong.employee_management.model.Employee;
import com.xuancong.employee_management.repository.DepartmentRepository;
import com.xuancong.employee_management.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor

public class DashboardService {
    private final EmployeeRepository employeeRepository;
    private DepartmentRepository departmentRepository;

    public DashboardResponse dashboard(){
        long employeeCount = employeeRepository.count();
        long departmentCount = departmentRepository.count();
        long totalLocations = 5;
        long pendingRequests = 5;
        DashboardSummaryResponse summaryResponse =  new DashboardSummaryResponse(
                employeeCount,
                departmentCount,
                totalLocations,
                pendingRequests
        );



        List<Employee> employees = employeeRepository.findAll();
        List<EmployeesByDepartment> employeesByDepartments =
                employees
                        .stream()
                        .collect(Collectors.groupingBy(
                                employee -> employee.getDepartment().getName(),
                                Collectors.counting()
                        ))
                        .entrySet()
                        .stream()
                        .map(entry -> new EmployeesByDepartment(
                                entry.getKey(),
                                entry.getValue()
                        ))
                        .toList();


        Map<EmployeeStatus, Long> map =
                employees.stream()
                        .collect(Collectors.groupingBy(
                                Employee::getStatus,
                                Collectors.counting()
                        ));


        EmployeeStatusResponse employeeStatusResponse = new EmployeeStatusResponse(
                map.getOrDefault(EmployeeStatus.ACTIVE,0L),
                map.getOrDefault(EmployeeStatus.ONLEAVE,0L),
                map.getOrDefault(EmployeeStatus.PENDING,0L)

        );
        DashboardChartResponse chartResponse = new DashboardChartResponse(
                employeesByDepartments,
                employeeStatusResponse
        );
        return new DashboardResponse(
                summaryResponse,
                chartResponse
        );







    }

}
