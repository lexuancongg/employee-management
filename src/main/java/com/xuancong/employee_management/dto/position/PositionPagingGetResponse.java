package com.xuancong.employee_management.dto.position;

import java.util.List;

public record PositionPagingGetResponse(
        List<PositionGetResponse> content,
        int totalElements,
        int totalPages,
        boolean last
) {
}