package com.xuancong.employee_management.model;

import com.xuancong.employee_management.enums.PayrollStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "Payroll")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payroll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "salary_id", nullable = false)
    private Salary salary;

    private int month;
    private int year;

    private BigDecimal baseSalary;
    private BigDecimal allowance;
    private BigDecimal socialInsurance;
    private BigDecimal healthInsurance;
    private BigDecimal unemploymentInsurance;

    // Phát sinh theo tháng
    private BigDecimal bonus;
    private BigDecimal overtimePay;
    private BigDecimal tax;
    private BigDecimal otherDeduction;

    private BigDecimal netSalary;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PayrollStatus status;

    private LocalDate paymentDate;

    @Column(columnDefinition = "TEXT")
    private String note;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


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