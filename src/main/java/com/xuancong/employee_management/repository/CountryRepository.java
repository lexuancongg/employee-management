package com.xuancong.employee_management.repository;

import com.xuancong.employee_management.model.Country;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Long> {
    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);

    Page<Country> findAllByNameContainingIgnoreCase(String name, Pageable pageable);
}
