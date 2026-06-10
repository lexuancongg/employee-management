package com.xuancong.employee_management.model;

import com.xuancong.employee_management.enums.DepartmentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "department")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Department extends BaseAuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @OneToOne
    @JoinColumn(name = "manager_id")
    private Employee manager;
    private String departmentCode;

    @Enumerated(EnumType.STRING)
    private DepartmentStatus status;

    @OneToMany
    private List<Employee> employees ;


}
