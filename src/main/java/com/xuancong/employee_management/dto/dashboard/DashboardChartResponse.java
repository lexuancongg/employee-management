package com.xuancong.employee_management.dto.dashboard;

import java.util.List;

public record DashboardChartResponse(
        List<EmployeesByDepartment> employeesByDepartment,
        EmployeeStatusResponse employeeStatusResponse
) {
}
