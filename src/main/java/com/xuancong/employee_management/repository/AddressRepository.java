package com.xuancong.employee_management.repository;

import com.xuancong.employee_management.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
    boolean existsByCountry_Id(Long countryId);
}
