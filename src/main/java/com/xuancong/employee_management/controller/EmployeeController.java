package com.xuancong.employee_management.controller;

import com.xuancong.employee_management.dto.employee.EmployeeCreateRequest;
import com.xuancong.employee_management.dto.employee.EmployeeGetResponse;
import com.xuancong.employee_management.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class EmployeeController {
    private final EmployeeService employeeService;

    @PostMapping("/employees")
    public ResponseEntity<EmployeeGetResponse> createEmployee(
            @Valid @RequestBody EmployeeCreateRequest employeeCreateRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.employeeService.createEmployee(employeeCreateRequest));

    }


    @PutMapping("/employees/{id}")
    public ResponseEntity<Void> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeCreateRequest employeeCreateRequest
    ){
        this.employeeService.updateEmployee(id, employeeCreateRequest);
        return ResponseEntity.noContent().build();
    }

}
