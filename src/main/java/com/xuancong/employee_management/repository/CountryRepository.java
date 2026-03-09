package com.xuancong.employee_management.repository;

import com.xuancong.employee_management.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Long> {
}
