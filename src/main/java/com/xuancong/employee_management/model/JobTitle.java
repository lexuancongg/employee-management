package com.xuancong.employee_management.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "job_title")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobTitle extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;
//
//    @OneToMany(mappedBy = "jobTitle")
//    private List<Employee> employees;
}
