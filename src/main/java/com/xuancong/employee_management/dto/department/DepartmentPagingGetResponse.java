package com.xuancong.employee_management.dto.department;

import java.util.List;

public record DepartmentPagingGetResponse(
        List<DepartmentGetResponse> content,
        int totalElements,
        int totalPages,
        boolean isLast
) {
}
