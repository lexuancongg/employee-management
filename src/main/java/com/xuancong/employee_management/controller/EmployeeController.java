package com.xuancong.employee_management.controller;

import com.xuancong.employee_management.constants.Constants;
import com.xuancong.employee_management.dto.employee.EmployeeCreateRequest;
import com.xuancong.employee_management.dto.employee.EmployeeDetailGetResponse;
import com.xuancong.employee_management.dto.employee.EmployeeGetResponse;
import com.xuancong.employee_management.dto.employee.EmployeePagingGetResponse;
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


    @GetMapping("/employees/{id}")
    public ResponseEntity<EmployeeDetailGetResponse> getEmployee(@PathVariable Long id) {
        return ResponseEntity.ok(
                this.employeeService.getEmployee(id)
        );
    }

    @GetMapping("/employees")
    public ResponseEntity<EmployeePagingGetResponse> getEmployees(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(defaultValue = Constants.Paging.DEFAULT_PAGE_NUMBER) int pageIndex,
            @RequestParam(defaultValue = Constants.Paging.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(defaultValue = "id,desc") String sort
    ) {
        return ResponseEntity.ok(
                employeeService.getEmployees(name, code, email, departmentId, pageIndex, pageSize, sort)
        );
    }

    @DeleteMapping("/employees/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        this.employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

}
