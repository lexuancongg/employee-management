package com.xuancong.employee_management.dto.position;

import java.util.List;

public record PositionPagingResponse(
        List<PositionResponse> content,
        int totalElements,
        int totalPages,
        boolean last
) {
}