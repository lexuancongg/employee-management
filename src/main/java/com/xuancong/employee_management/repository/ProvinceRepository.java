package com.xuancong.employee_management.repository;

import com.xuancong.employee_management.model.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collection;
import java.util.List;

public interface ProvinceRepository extends JpaRepository<Province, Long>, JpaSpecificationExecutor<Province> {
    boolean existsByCountry_Id(Long countryId);

    boolean existsByNameIgnoreCaseAndCountryIdAndIdNot(String name, Long countryId, Long id);

    List<Province> findAllByCountry_Id(Long countryId);
}
