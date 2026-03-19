package com.xuancong.employee_management.controller;

import com.xuancong.employee_management.constants.Constants;
import com.xuancong.employee_management.dto.paging.PageResponse;
import com.xuancong.employee_management.dto.department.DepartmentCreateRequest;
import com.xuancong.employee_management.dto.department.DepartmentResponse;
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

        @PostMapping("/management/departments")
    public ResponseEntity<DepartmentResponse> createDepartment(@Valid @RequestBody DepartmentCreateRequest departmentCreateRequest) {
        return ResponseEntity.ok(
                this.departmentService.createDepartment(departmentCreateRequest)
        );

    }
    @PutMapping("/management/departments/{id}")
    public ResponseEntity<Void> updateDepartment(
            @PathVariable Long id,
            @Valid @RequestBody DepartmentCreateRequest departmentCreateRequest
    ){
        this.departmentService.updateDepartment(id,departmentCreateRequest);
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/management/departments/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        this.departmentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/management/departments")
    public ResponseEntity<PageResponse<DepartmentResponse>> getDepartments(
            @RequestParam(name = "pageIndex",required = false,defaultValue = Constants.Paging.DEFAULT_PAGE_NUMBER) int pageIndex,
            @RequestParam(name = "pageSize", required = false,defaultValue = Constants.Paging.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(name = "keyword",required = false,defaultValue = "") String keyword,
            @RequestParam(name = "branchId",required = false) final  Long branchId
    ) {
        return ResponseEntity
                .ok(this.departmentService.getDepartments(pageIndex,pageSize,keyword,branchId));
    }


}
