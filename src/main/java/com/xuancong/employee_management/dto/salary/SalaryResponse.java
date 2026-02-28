package com.xuancong.employee_management.dto.salary;

import com.xuancong.employee_management.model.Salary;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SalaryResponse(
        Long id,
        Long employeeId,
        BigDecimal baseSalary,
        LocalDate effectiveDate,
        LocalDate endDate,
        boolean active,
        String note
) {
    public static SalaryResponse fromSalary(Salary salary) {
        return new SalaryResponse(
                salary.getId(),
                salary.getEmployee().getId(),
                salary.getBaseSalary(),
                salary.getEffectiveDate(),
                salary.getEndDate(),
                salary.isActive(),
                salary.getNote()
        );
    }
}
