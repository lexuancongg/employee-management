package com.xuancong.employee_management.dto.salary;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SalaryCreateRequest(
        BigDecimal baseSalary,
        LocalDate effectiveDate,
        String note
) {
}
