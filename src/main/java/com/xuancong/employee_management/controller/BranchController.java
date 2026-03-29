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

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BranchController {
    private final BranchService branchService;

    // pass
    @PostMapping("/management/branchs")
    public ResponseEntity<BranchResponse> create(@Valid @RequestBody BranchCreateRequest branchCreateRequest){
        return ResponseEntity.ok(branchService.create(branchCreateRequest));
    }






    // pass
    @DeleteMapping("/management/branchs/{id}")
    public ResponseEntity<BranchResponse> delete(@PathVariable Long id){
        branchService.delete(id);
        return ResponseEntity.ok().build();
    }



    // pass
    @PutMapping("/management/branchs/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id,
                                       @Valid @RequestBody BranchCreateRequest branchCreateRequest){
        branchService.update(id, branchCreateRequest);
        return ResponseEntity.noContent().build();

    }



    // pass
    @GetMapping("/management/branchs")
    public ResponseEntity<PageResponse<BranchResponse>> getBranchs(
            @RequestParam(name = "keyword",defaultValue = "" ,required = false) String keyword,
            @RequestParam(name = "pageSize", defaultValue = Constants.Paging.DEFAULT_PAGE_SIZE,required = false) int pageSize,
            @RequestParam(name = "pageIndex",defaultValue = Constants.Paging.DEFAULT_PAGE_NUMBER,required = false) int pageIndex,
            @RequestParam(value = "countryId",required = false) final Long countryId,
            @RequestParam(name = "provinceId",required = false) final Long provinceId


            ){
        return ResponseEntity.ok(
                branchService.getBranchs(pageIndex,pageSize,keyword,countryId,provinceId)
        );
    }

    // pass
    @GetMapping("/employee/branchs")
    public ResponseEntity<List<BranchResponse>> getAllBranches(){
        return  ResponseEntity.ok(
                branchService.getAllBranches()
        );
    }


}
