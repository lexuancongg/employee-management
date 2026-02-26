package com.xuancong.employee_management.dto.attendance;

import jakarta.validation.constraints.NotNull;

public record CheckInRequest(
      @NotNull Long employeeId
) {
}
