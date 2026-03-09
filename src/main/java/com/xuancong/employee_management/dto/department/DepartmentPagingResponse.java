package com.xuancong.employee_management.dto.department;

import java.util.List;

public record DepartmentPagingResponse(
        List<DepartmentResponse> content,
        int totalElements,
        int totalPages,
        boolean isLast
) {
}
