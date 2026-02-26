package com.xuancong.employee_management.dto.attendance;

import com.xuancong.employee_management.dto.department.DepartmentGetResponse;
import com.xuancong.employee_management.dto.employee.EmployeeGetResponse;
import com.xuancong.employee_management.dto.position.PositionGetResponse;
import com.xuancong.employee_management.model.Attendance;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record AttendanceGetResponse (
    EmployeeGetResponse employee,
    DepartmentGetResponse department,
    PositionGetResponse position,
    LocalDate workDate,
    LocalDateTime checkIn,
    LocalDateTime checkOut
    ){
    public static AttendanceGetResponse fromAttendance(Attendance attendance) {
        return new AttendanceGetResponse(
                EmployeeGetResponse.fromEmployee(attendance.getEmployee()),
                DepartmentGetResponse.fromDepartment(attendance.getEmployee().getDepartment()),
                PositionGetResponse.fromPosition(attendance.getEmployee().getPosition()),
                attendance.getWorkDate(),
                attendance.getCheckIn(),
                attendance.getCheckOut()
        );
    }

}
