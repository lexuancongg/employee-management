package com.xuancong.employee_management.service;

import com.xuancong.employee_management.constants.Constants;
import com.xuancong.employee_management.dto.paging.PageResponse;
import com.xuancong.employee_management.dto.salary.SalaryCreateRequest;
import com.xuancong.employee_management.dto.salary.SalaryDetailResponse;
import com.xuancong.employee_management.dto.salary.SalaryResponse;
import com.xuancong.employee_management.exception.NotFoundException;
import com.xuancong.employee_management.model.Employee;
import com.xuancong.employee_management.model.Salary;
import com.xuancong.employee_management.repository.EmployeeRepository;
import com.xuancong.employee_management.repository.SalaryRepository;
import com.xuancong.employee_management.specification.SalarySpecification;
import com.xuancong.employee_management.utils.AuthenticationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SalaryService {
    private final SalaryRepository salaryRepository;
    private final EmployeeRepository employeeRepository;

    public SalaryResponse createSalary(
                                       SalaryCreateRequest req) {

        Employee employee = validateExitingEmployee(req.employeeId());

        boolean isFirst =
                !salaryRepository.existsByEmployee_IdAndActiveTrue(req.employeeId());

        if (!isFirst) {
            Salary activeSalary =
                    salaryRepository.findByEmployee_IdAndActiveTrue(req.employeeId())
                            .orElseThrow(() -> new NotFoundException(Constants.ErrorKey.EMPLOYEE_NOT_FOUND));

            activeSalary.setActive(false);
            activeSalary.setEndDate(req.effectiveDate().minusDays(1));
            salaryRepository.save(activeSalary);
        }

        Salary salary = Salary.builder()
                .employee(employee)
                .baseSalary(req.baseSalary())
                .allowance(req.allowance())
                .socialInsurance(req.baseSalary().multiply(new BigDecimal("0.08")))
                .healthInsurance(req.baseSalary().multiply(new BigDecimal("0.015")))
                .unemploymentInsurance(req.baseSalary().multiply(new BigDecimal("0.01")))
                .effectiveDate(req.effectiveDate())
                .note(req.note())
                .active(true)
                .build();

        salaryRepository.save(salary);
        return SalaryResponse.fromSalary(salary);
    }



    private Employee validateExitingEmployee(Long employeeId){
        return employeeRepository.findById(employeeId)
                .orElseThrow(()->new NotFoundException("Employee not found"));
    }

    public SalaryResponse getCurrentSalary(){
        Long userId   = AuthenticationUtils.extractUserId();
        Employee employee = employeeRepository.findByUser_Id(userId)
                .orElseThrow(() -> new NotFoundException(Constants.ErrorKey.EMPLOYEE_NOT_FOUND));
        Salary salary = salaryRepository.findByEmployee_IdAndActiveTrue(employee.getId())
                .orElse(new Salary());
        return SalaryResponse.fromSalary(salary);
    }


    public List<SalaryDetailResponse> getSalary(Long employeeId){
        Employee employee = validateExitingEmployee(employeeId);
        Sort sort = Sort.by(
                Sort.Order.desc("active"),
                Sort.Order.desc("effectiveDate")
        );

        List<Salary> salaries =
                salaryRepository.findByEmployee(employee, sort);
        return  salaries.stream()
                .map(SalaryDetailResponse::fromSalary)
                .toList();


    }


    public PageResponse<SalaryDetailResponse> getSalariesFilter(
           String keyword,
           String email,
           Long branchId,
           Long departmentId,
           Long positionId,
           int pageIndex,
          int  pageSize
    ){
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        Specification<Salary> specification = SalarySpecification.filter(
                keyword,
                email,
                branchId,
                departmentId,
                positionId
        );

        Page<Salary> salaryPage = this.salaryRepository.findAll(specification, PageRequest.of(pageIndex, pageSize));

        List<Salary> salaries = salaryPage.getContent();

        List<SalaryDetailResponse> content = salaries.stream()
                .map(SalaryDetailResponse::fromSalary)
                .toList();

        return new PageResponse<SalaryDetailResponse>(
                content,
                (int) salaryPage.getTotalElements(),
                salaryPage.getTotalPages(),
                salaryPage.isLast()
        );


    }

    public SalaryDetailResponse getSalaryById(Long id){
        Salary salary = salaryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Constants.ErrorKey.EMPLOYEE_NOT_FOUND));
        return SalaryDetailResponse.fromSalary(salary);
    }


}
