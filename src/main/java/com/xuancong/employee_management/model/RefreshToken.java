package com.xuancong.employee_management.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "refreshToken")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken {
    @Id
    private String id;

    @Column(nullable = false, unique = true)
    private String refreshToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
