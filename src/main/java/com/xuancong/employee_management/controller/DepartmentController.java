package com.xuancong.employee_management.controller;

import com.xuancong.employee_management.dto.department.DepartmentCreateRequest;
import com.xuancong.employee_management.dto.department.DepartmentGetResponse;
import com.xuancong.employee_management.model.Department;
import com.xuancong.employee_management.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @PutMapping("/departments/{id}")
    public ResponseEntity<Void> updateDepartment(
            @PathVariable Long id,
            @Valid @RequestBody DepartmentCreateRequest departmentCreateRequest
    ){
        this.departmentService.updateDepartment(id,departmentCreateRequest);
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/departments/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        this.departmentService.delete(id);
        return ResponseEntity.noContent().build();
    }


}
