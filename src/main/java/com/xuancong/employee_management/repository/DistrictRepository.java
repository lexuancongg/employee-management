package com.xuancong.employee_management.repository;

import com.xuancong.employee_management.model.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DistrictRepository extends JpaRepository<District, Long>, JpaSpecificationExecutor<District> {
    boolean existsByProvince_Id(Long provinceId);

    @Query("""
                SELECT d FROM District d
                JOIN FETCH d.province p
                JOIN FETCH p.country c
                WHERE p.id = :provinceId
            """)
    List<District> findAllWithProvinceAndCountryByProvinceId(@Param("provinceId") Long provinceId);

    boolean existsByNameIgnoreCaseAndProvince_IdAndIdNot(String name, Long provinceId, Long id);
}
