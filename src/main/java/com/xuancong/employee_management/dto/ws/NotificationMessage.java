package com.xuancong.employee_management.dto.ws;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotificationMessage {
    private Long userId;
    private String title;
    private String message;
    private String type;
}