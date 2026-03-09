package com.xuancong.employee_management.dto.attendance;

import com.xuancong.employee_management.dto.department.DepartmentResponse;
import com.xuancong.employee_management.dto.employee.EmployeeResponse;
import com.xuancong.employee_management.dto.position.PositionResponse;
import com.xuancong.employee_management.model.Attendance;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record AttendanceResponse(
    EmployeeResponse employee,
    DepartmentResponse department,
    PositionResponse position,
    LocalDate workDate,
    LocalDateTime checkIn,
    LocalDateTime checkOut
    ){
    public static AttendanceResponse fromAttendance(Attendance attendance) {
        return new AttendanceResponse(
                EmployeeResponse.fromEmployee(attendance.getEmployee()),
                DepartmentResponse.fromDepartment(attendance.getEmployee().getDepartment()),
                PositionResponse.fromPosition(attendance.getEmployee().getPosition()),
                attendance.getWorkDate(),
                attendance.getCheckIn(),
                attendance.getCheckOut()
        );
    }

}
