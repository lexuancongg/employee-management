package com.xuancong.employee_management.dto.attendance;

import java.util.List;

public record AttendancePagingGetResponse(
        List<AttendanceGetResponse> content,
        int totalElements,
        int totalPages,
        boolean isLast
) {
}
