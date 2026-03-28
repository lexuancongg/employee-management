package com.xuancong.employee_management.controller;

import com.xuancong.employee_management.constants.Constants;
import com.xuancong.employee_management.dto.paging.PageResponse;
import com.xuancong.employee_management.dto.salary.SalaryCreateRequest;
import com.xuancong.employee_management.dto.salary.SalaryDetailResponse;
import com.xuancong.employee_management.dto.salary.SalaryResponse;
import com.xuancong.employee_management.service.SalaryService;
import lombok.RequiredArgsConstructor;
import org.aspectj.apache.bcel.classfile.Constant;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SalaryController {
    private final SalaryService salaryService;

    @PostMapping("/management/salaries")
    public ResponseEntity<SalaryResponse> createSalary(
                                                       @RequestBody SalaryCreateRequest salaryCreateRequest) {
        return ResponseEntity.ok(
                this.salaryService.createSalary(salaryCreateRequest)
        );
    }

    @GetMapping("/employees/salaries/me/current")
    public ResponseEntity<SalaryResponse> getCurrentSalary() {
        return ResponseEntity.ok(
                this.salaryService.getCurrentSalary()
        );
    }

    @GetMapping("/management/salaries/employee/{employeeId}")
    public ResponseEntity<List<SalaryDetailResponse>> getSalary(@PathVariable("employeeId") Long employeeId) {
        return ResponseEntity.ok(
                salaryService.getSalary(employeeId)
        );
    }


    @GetMapping("/management/salaries")
    public ResponseEntity<PageResponse<SalaryDetailResponse>> getSalaries(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Long branchId,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) Long positionId,
            @RequestParam(required = false) Boolean active,
            @RequestParam(defaultValue = Constants.Paging.DEFAULT_PAGE_NUMBER) int pageIndex,
            @RequestParam(defaultValue = Constants.Paging.DEFAULT_PAGE_SIZE) int pageSize

    ){
        return  ResponseEntity.ok(
                salaryService.getSalariesFilter(
                        keyword,
                        email,
                        branchId,
                        departmentId,
                        positionId,
                        pageIndex,
                        pageSize
                )
        );
    }

    @GetMapping("/management/salaries/{id}")
    public ResponseEntity<SalaryDetailResponse> getSalaryById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(
                salaryService.getSalaryById(id)
        );
    }





}
