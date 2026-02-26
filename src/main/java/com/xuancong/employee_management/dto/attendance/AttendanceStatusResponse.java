package com.xuancong.employee_management.dto.attendance;

import com.xuancong.employee_management.enums.AttendanceStatus;
import com.xuancong.employee_management.model.Attendance;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record AttendanceStatusResponse(
        Long employeeId,
        LocalDate workDate,
        LocalDateTime checkIn,
        LocalDateTime checkOut,
        AttendanceStatus status

) {
    public static AttendanceStatusResponse from(Attendance attendance ,Long employeeId,LocalDate workDate) {
        return new AttendanceStatusResponse(
                employeeId,
                workDate,
                attendance == null ? null : attendance.getCheckIn(),
                attendance == null ? null : attendance.getCheckOut(),
                attendance ==  null ? AttendanceStatus.NOT_CHECKIN : attendance.getCheckOut() == null ? AttendanceStatus.NOT_CHECKOUT : AttendanceStatus.COMPLETED

        );
    }
}
