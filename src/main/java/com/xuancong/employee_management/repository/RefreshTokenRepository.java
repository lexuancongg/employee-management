package com.xuancong.employee_management.repository;

import com.xuancong.employee_management.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
}
