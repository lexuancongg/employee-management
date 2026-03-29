package com.xuancong.employee_management.repository;

import com.xuancong.employee_management.model.Country;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CountryRepository extends JpaRepository<Country, Long>, JpaSpecificationExecutor<Country> {
    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);

    Page<Country> findAllByNameContainingIgnoreCase(String name, Pageable pageable);
}
