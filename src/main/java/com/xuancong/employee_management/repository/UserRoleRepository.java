package com.xuancong.employee_management.repository;

import com.xuancong.employee_management.model.User;
import com.xuancong.employee_management.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    List<UserRole> findByUser(User user);
}
