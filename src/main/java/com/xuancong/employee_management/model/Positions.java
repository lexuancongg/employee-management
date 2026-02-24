package com.xuancong.employee_management.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "Positions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Positions extends BaseAuditEntity{
    @Id
    private Long id;
    private String name;
    // level

}
