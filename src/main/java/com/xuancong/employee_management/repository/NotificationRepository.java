package com.xuancong.employee_management.repository;

import com.xuancong.employee_management.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification,Long> {
}
