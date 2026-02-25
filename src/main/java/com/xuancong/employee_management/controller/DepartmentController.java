package com.xuancong.employee_management.controller;

import com.xuancong.employee_management.dto.department.DepartmentCreateRequest;
import com.xuancong.employee_management.dto.department.DepartmentGetResponse;
import com.xuancong.employee_management.model.Department;
import com.xuancong.employee_management.service.DepartmentService;
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
public class DepartmentController {
    private final DepartmentService departmentService;

    @PostMapping("/departments")
    public ResponseEntity<DepartmentGetResponse> createDepartment(@Valid @RequestBody DepartmentCreateRequest departmentCreateRequest) {
        return ResponseEntity.ok(
                this.departmentService.createDepartment(departmentCreateRequest)
        );

    }

}
