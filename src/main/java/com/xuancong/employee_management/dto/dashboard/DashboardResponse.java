package com.xuancong.employee_management.dto.dashboard;

public record DashboardResponse(
        DashboardSummaryResponse summary,
        DashboardChartResponse charts
) {
}
