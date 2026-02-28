package com.xuancong.employee_management.controller;

import com.xuancong.employee_management.constants.Constants;
import com.xuancong.employee_management.dto.attendance.AttendanceGetResponse;
import com.xuancong.employee_management.dto.attendance.AttendancePagingGetResponse;
import com.xuancong.employee_management.dto.attendance.AttendanceStatusResponse;
import com.xuancong.employee_management.dto.attendance.CheckInRequest;
import com.xuancong.employee_management.service.AttendanceService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AttendanceController {
    private final AttendanceService attendanceService;

    @PostMapping("/attendances/check-in")
    public ResponseEntity<AttendanceGetResponse> checkIn(){
        return ResponseEntity.ok(
                this.attendanceService.checkIn()
        );
    }

    @PutMapping("/attendances/check-out")
    public ResponseEntity<Void> checkOut(){
        attendanceService.checkOut();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/attendances/today")
    public ResponseEntity<AttendanceStatusResponse> getTodayAttendance(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(attendanceService.getTodayAttendance());
    }



    @GetMapping("/attendances")
    public ResponseEntity<AttendancePagingGetResponse> getAttendances(
            @RequestParam(name = "from",required = false)LocalDate fromDate,
            @RequestParam(name = "to",required = false)LocalDate toDate,
            @RequestParam(name = "pageIndex",required = false,defaultValue = Constants.Paging.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(name = "pageSize",required = false,defaultValue = Constants.Paging.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(name = "employeeId", required = false) long employeeId
            ){
        return ResponseEntity.ok(
                attendanceService.getAttendances(fromDate,toDate,page,pageSize,employeeId)
        );

    }



}
