package com.xuancong.employee_management.controller;

import com.xuancong.employee_management.constants.Constants;
import com.xuancong.employee_management.dto.paging.PageResponse;
import com.xuancong.employee_management.dto.branch.BranchCreateRequest;
import com.xuancong.employee_management.dto.branch.BranchResponse;
import com.xuancong.employee_management.service.BranchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id,
                                       @Valid @RequestBody BranchCreateRequest branchCreateRequest){
        branchService.update(id, branchCreateRequest);
        return ResponseEntity.noContent().build();

    }


    @GetMapping()
    public ResponseEntity<PageResponse<BranchResponse>> getBranchs(
            @RequestParam(name = "keyword",defaultValue = "" ,required = false) String keyword,
            @RequestParam(name = "pageSize", defaultValue = Constants.Paging.DEFAULT_PAGE_SIZE,required = false) int pageSize,
            @RequestParam(name = "pageIndex",defaultValue = Constants.Paging.DEFAULT_PAGE_NUMBER,required = false) int pageIndex

    ){
        return ResponseEntity.ok(
                branchService.getBranchs(pageIndex,pageSize,keyword)
        );
    }


}
