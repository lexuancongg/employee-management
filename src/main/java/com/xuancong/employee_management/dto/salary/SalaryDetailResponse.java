package com.xuancong.employee_management.dto.salary;

import com.xuancong.employee_management.model.Salary;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
@Builder
public record SalaryDetailResponse(
        Long id,
        Long employeeId,
        String employeeName,
        String branchName,
        String departmentName,
        String positionName,
        String email,
        BigDecimal baseSalary,
        BigDecimal allowance,
        BigDecimal socialInsurance,
        BigDecimal healthInsurance,
        BigDecimal unemploymentInsurance,
        LocalDate effectiveDate,
        boolean active,
        String note
) {

    public static  SalaryDetailResponse fromSalary(Salary salary) {
        return SalaryDetailResponse.builder()
                .id(salary.getId())
                .active(salary.isActive())
                .baseSalary(salary.getBaseSalary())
                .email(salary.getEmployee().getEmail())
                .employeeName(salary.getEmployee().getName())
                .branchName(salary.getEmployee().getDepartment().getBranch().getName())
                .departmentName(salary.getEmployee().getDepartment().getName())
                .positionName(salary.getEmployee().getPosition().getName())
                .allowance(salary.getAllowance())
                .socialInsurance(salary.getSocialInsurance())
                .healthInsurance(salary.getHealthInsurance())
                .unemploymentInsurance(salary.getUnemploymentInsurance())
                .effectiveDate(salary.getEffectiveDate())
                .note(salary.getNote())
                .build();
    }
}
