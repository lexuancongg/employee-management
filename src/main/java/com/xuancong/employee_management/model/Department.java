package com.xuancong.employee_management.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Department")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Department extends BaseAuditEntity {
    @Id
    private Long id;
    private String name;
    private String description;



}
