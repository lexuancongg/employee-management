package com.xuancong.employee_management.model;

import com.xuancong.employee_management.enums.EmployeeStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "Employee")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee extends BaseAuditEntity {
    @Id
    private Long id;
    private String employeeCode;
    private String name;
    private String gender;
    private String email;
    private String phone;
    private LocalDate birthday;
    private LocalDate hireDate;
    private EmployeeStatus status;
    private Long avatarId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id",nullable = false)
    private Position position;


    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "department_id",nullable = false)
    private Department department;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

}
