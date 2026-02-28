package com.xuancong.employee_management.controller;

import com.xuancong.employee_management.dto.salary.SalaryCreateRequest;
import com.xuancong.employee_management.dto.salary.SalaryGetResponse;
import com.xuancong.employee_management.dto.salary.SalaryResponse;
import com.xuancong.employee_management.service.SalaryService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SalaryController {
    private final SalaryService salaryService;

    @PostMapping("/employees/{employeeId}/salaries")
    public ResponseEntity<SalaryResponse> createSalary(@PathVariable("employeeId") Long employeeId,
                                                       @RequestBody SalaryCreateRequest salaryCreateRequest) {
        return ResponseEntity.ok(
                this.salaryService.createSalary(employeeId,salaryCreateRequest)
        );
    }

    @GetMapping("/employees/salaries/me/current")
    public ResponseEntity<SalaryResponse> getCurrentSalary() {
        return ResponseEntity.ok(
                this.salaryService.getCurrentSalary()
        );
    }

    @GetMapping("/employees/{employeeId}/salaries")
    public ResponseEntity<List<SalaryResponse>> getSalary(@PathVariable("employeeId") Long employeeId) {
        return ResponseEntity.ok(
                salaryService.getSalary(employeeId)
        );
    }


}
