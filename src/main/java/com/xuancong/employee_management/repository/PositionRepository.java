package com.xuancong.employee_management.repository;

import com.xuancong.employee_management.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionRepository extends JpaRepository<Position, Long> {

    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);
}
