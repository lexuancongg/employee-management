package com.xuancong.employee_management.controller;

import com.xuancong.employee_management.constants.Constants;
import com.xuancong.employee_management.dto.paging.PageResponse;
import com.xuancong.employee_management.dto.employee.EmployeeCreateRequest;
import com.xuancong.employee_management.dto.employee.EmployeeDetailResponse;
import com.xuancong.employee_management.dto.employee.EmployeeResponse;
import com.xuancong.employee_management.enums.EmployeeStatus;
import com.xuancong.employee_management.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class EmployeeController {
    private final EmployeeService employeeService;

    @PostMapping("/management/employees")
    public ResponseEntity<EmployeeResponse> createEmployee(
            @Valid @RequestBody EmployeeCreateRequest employeeCreateRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.employeeService.createEmployee(employeeCreateRequest));

    }


    @PutMapping("/management/employees/{id}")
    public ResponseEntity<Void> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeCreateRequest employeeCreateRequest
    ){
        this.employeeService.updateEmployee(id, employeeCreateRequest);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/management/employees/{id}")
    public ResponseEntity<EmployeeDetailResponse> getEmployee(@PathVariable Long id) {
        return ResponseEntity.ok(
                this.employeeService.getEmployee(id)
        );
    }

    @GetMapping("/management/employees")
    public ResponseEntity<PageResponse<EmployeeResponse>> getEmployees(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) Long branchId,
            @RequestParam(required = false) Long positionId,
            @RequestParam(required = false) EmployeeStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hireDateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hireDateTo,
            @RequestParam(defaultValue = Constants.Paging.DEFAULT_PAGE_NUMBER) int pageIndex,
            @RequestParam(defaultValue = Constants.Paging.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        return ResponseEntity.ok(
                employeeService.getEmployees(
                        name, code, email, departmentId, branchId, positionId, status,
                        hireDateFrom, hireDateTo,
                        pageIndex, pageSize, sortBy,
                        sortDir
                )
        );
    }

    @DeleteMapping("/management/employees/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        this.employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }



}
