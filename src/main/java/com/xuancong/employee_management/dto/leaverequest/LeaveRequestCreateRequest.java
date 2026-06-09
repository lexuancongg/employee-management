package com.xuancong.employee_management.dto.leaverequest;

import com.xuancong.employee_management.model.LeaveType;
import lombok.Data;

import java.time.LocalDate;
@Data
public class LeaveRequestCreateRequest {
    private LeaveType leaveType;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
}
