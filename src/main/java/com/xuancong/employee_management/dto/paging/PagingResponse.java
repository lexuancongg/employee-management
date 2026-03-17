package com.xuancong.employee_management.dto.paging;


import lombok.Builder;

import java.util.List;

@Builder
public record PagingResponse<T>(
        List<T> payload,
        int pageIndex,
        int pageSize,
        long totalElements,
        int totalPages,
        boolean last
) {}