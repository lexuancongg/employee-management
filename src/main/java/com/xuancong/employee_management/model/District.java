package com.xuancong.employee_management.model;

import jakarta.persistence.*;
import lombok.*;
import software.amazon.awssdk.services.s3.endpoints.internal.Identifier;

@Entity
@Table(name = "district")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class District {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "province_id")
    private Province province;

}
