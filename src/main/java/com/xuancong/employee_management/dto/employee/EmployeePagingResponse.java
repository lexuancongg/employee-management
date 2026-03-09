package com.xuancong.employee_management.dto.employee;

import java.util.List;

public record EmployeePagingResponse(
        List<EmployeeResponse> content,
        int totalElements,
        int totalPages,
        boolean last
) {
}
