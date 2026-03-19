package com.xuancong.employee_management.repository;

import com.xuancong.employee_management.model.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface DistrictRepository extends JpaRepository<District, Long>, JpaSpecificationExecutor<District> {
    boolean existsByProvince_Id(Long provinceId);

    List<District> findAllByProvince_Id(Long provinceId);

    boolean existsByNameIgnoreCaseAndProvince_IdAndIdNot(String name, Long provinceId, Long id);
}
