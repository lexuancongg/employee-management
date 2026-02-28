package com.xuancong.employee_management.service;

import com.xuancong.employee_management.constants.Constants;
import com.xuancong.employee_management.dto.salary.SalaryCreateRequest;
import com.xuancong.employee_management.dto.salary.SalaryResponse;
import com.xuancong.employee_management.exception.NotFoundException;
import com.xuancong.employee_management.model.Employee;
import com.xuancong.employee_management.model.Salary;
import com.xuancong.employee_management.repository.EmployeeRepository;
import com.xuancong.employee_management.repository.SalaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SalaryService {
    private final SalaryRepository salaryRepository;
    private final EmployeeRepository employeeRepository;

    public SalaryResponse createSalary(Long employeeId,
                                       SalaryCreateRequest req) {

        Employee employee = validateExitingEmployee(employeeId);

        boolean isFirst =
                !salaryRepository.existsByEmployeeIdAndActiveTrue(employeeId);

        if (!isFirst) {
            Salary activeSalary =
                    salaryRepository.findByEmployeeIdAndActiveTrue(employeeId)
                            .orElseThrow(() -> new NotFoundException(Constants.ErrorKey.EMPLOYEE_NOT_FOUND));

            activeSalary.setActive(false);
            activeSalary.setEndDate(req.effectiveDate().minusDays(1));
        }

        Salary salary = Salary.builder()
                .note(req.note())
                .baseSalary(req.baseSalary())
                .effectiveDate(req.effectiveDate())
                .employee(employee)
                .isActive(true)
                .build();

        salaryRepository.save(salary);
        return SalaryResponse.fromSalary(salary);
    }



    private Employee validateExitingEmployee(Long employeeId){
        return employeeRepository.findById(employeeId)
                .orElseThrow(()->new NotFoundException("Employee not found"));
    }



}
