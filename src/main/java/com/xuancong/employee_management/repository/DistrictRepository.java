package com.xuancong.employee_management.repository;

import com.xuancong.employee_management.model.District;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DistrictRepository extends JpaRepository<District, Long> {
    boolean existsByProvince_Id(Long provinceId);

    List<District> findAllByProvince_Id(Long provinceId);
}
