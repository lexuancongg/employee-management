package com.xuancong.employee_management.controller;

import com.xuancong.employee_management.dto.role.RoleCreateRequest;
import com.xuancong.employee_management.dto.role.RoleResponse;
import com.xuancong.employee_management.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RoleController {

    private final RoleService roleService;

    @PostMapping("/roles")
    public ResponseEntity<RoleResponse> createRole (@Valid @RequestBody RoleCreateRequest roleCreateRequest){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        roleService.create(roleCreateRequest)
                );
    }

    @PutMapping("/roles/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id ,
                                       @Valid @RequestBody RoleCreateRequest roleCreateRequest){
        roleService.updateRole(id, roleCreateRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/roles/id")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }

}
