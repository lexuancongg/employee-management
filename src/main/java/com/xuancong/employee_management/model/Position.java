package com.xuancong.employee_management.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "Position")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Position extends BaseAuditEntity{
    @Id
    private Long id;
    private String name;
    // level


    @OneToMany(mappedBy = "position")
    private List<Employee> employees;
}
