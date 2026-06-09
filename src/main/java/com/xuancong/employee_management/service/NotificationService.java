package com.xuancong.employee_management.service;

import com.xuancong.employee_management.dto.ws.NotificationMessage;
import com.xuancong.employee_management.model.Employee;
import com.xuancong.employee_management.model.Notification;
import com.xuancong.employee_management.model.NotificationType;
import com.xuancong.employee_management.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    private final NotificationRepository notificationRepository;

    public Notification createNotification(
            Employee user,
            String title,
            String message,
            NotificationType type,
            Long referenceId
    ) {
        Notification notification = Notification.builder()
                .user(user)
                .title(title)
                .message(message)
                .type(type)
                .referenceId(referenceId)
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build();

        return notificationRepository.save(notification);
    }


    public void sendToUser(Long userId, NotificationMessage message) {

        // gửi riêng từng user
        messagingTemplate.convertAndSendToUser(
                userId.toString(),
                "/queue/notifications",
                message
        );
    }
}