package com.xuancong.employee_management.dto.error;

import java.util.List;

public record ErrorResponse(
        int status,
        String title,
        String details,
        List<String> fieldErrors
) {
}
