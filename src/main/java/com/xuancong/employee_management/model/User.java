package com.xuancong.employee_management.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseAuditEntity {
    @Id
    private Long id;
    @Column(unique = true, nullable = false)
    private String username;
    private String password;
    private boolean enabled;
    private String email;
    private String phone;


}
