package com.xuancong.employee_management.dto.employee;

import com.xuancong.employee_management.dto.position.PositionGetResponse;

import java.util.List;

public record EmployeePagingGetResponse(
        List<EmployeeGetResponse> content,
        int totalElements,
        int totalPages,
        boolean last
) {
}
