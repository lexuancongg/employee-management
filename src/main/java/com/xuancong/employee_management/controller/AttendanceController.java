package com.xuancong.employee_management.controller;

import com.xuancong.employee_management.dto.attendance.AttendanceGetResponse;
import com.xuancong.employee_management.dto.attendance.CheckInRequest;
import com.xuancong.employee_management.service.AttendanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
