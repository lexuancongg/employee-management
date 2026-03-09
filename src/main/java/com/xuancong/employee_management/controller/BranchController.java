package com.xuancong.employee_management.controller;

import com.xuancong.employee_management.dto.branch.BranchCreateRequest;
import com.xuancong.employee_management.dto.branch.BranchResponse;
import com.xuancong.employee_management.service.BranchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/branchs")
public class BranchController {
    private final BranchService branchService;

    @PostMapping()
    public ResponseEntity<BranchResponse> create(@Valid @RequestBody BranchCreateRequest branchCreateRequest){
        return ResponseEntity.ok(branchService.create(branchCreateRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BranchResponse> delete(@PathVariable Long id){
        branchService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/id")
    public ResponseEntity<Void> update(@PathVariable Long id,
                                       @Valid @RequestBody BranchCreateRequest branchCreateRequest){

    }
}
