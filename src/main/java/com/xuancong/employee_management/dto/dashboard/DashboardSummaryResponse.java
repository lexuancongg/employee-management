package com.xuancong.employee_management.dto.dashboard;

public record DashboardSummaryResponse (
        long totalEmployees,
        long totalDepartments,
        long totalLocations,
        long pendingRequests
)
{

}
