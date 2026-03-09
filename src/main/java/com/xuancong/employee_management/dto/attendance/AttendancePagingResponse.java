package com.xuancong.employee_management.dto.attendance;

import java.util.List;

public record AttendancePagingResponse(
        List<AttendanceResponse> content,
        int totalElements,
        int totalPages,
        boolean isLast
) {
}
