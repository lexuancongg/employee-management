package com.xuancong.employee_management.model;

import com.xuancong.employee_management.model.Employee;
import com.xuancong.employee_management.model.Payroll;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "payroll_item",
        uniqueConstraints = @UniqueConstraint(columnNames = {"payroll_id", "employee_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayrollItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payroll_id", nullable = false)
    private Payroll payroll;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    private BigDecimal baseSalary;
    private BigDecimal allowance;

    private BigDecimal socialInsurance;
    private BigDecimal healthInsurance;
    private BigDecimal unemploymentInsurance;

    private BigDecimal bonus;
    private BigDecimal overtimePay;
    private BigDecimal tax;
    private BigDecimal otherDeduction;

    private BigDecimal netSalary;

    private Integer workingDays;

    @PrePersist
    @PreUpdate
    public void calculateNetSalary() {
        BigDecimal income = sum(baseSalary, allowance, bonus, overtimePay);
        BigDecimal deduction = sum(socialInsurance, healthInsurance, unemploymentInsurance, tax, otherDeduction);
        this.netSalary = income.subtract(deduction);
    }

    private BigDecimal sum(BigDecimal... values) {
        BigDecimal total = BigDecimal.ZERO;
        for (BigDecimal v : values) {
            if (v != null) total = total.add(v);
        }
        return total;
    }
}