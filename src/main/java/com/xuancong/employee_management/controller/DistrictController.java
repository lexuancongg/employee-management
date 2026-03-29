package com.xuancong.employee_management.controller;

import com.xuancong.employee_management.constants.Constants;
import com.xuancong.employee_management.dto.district.DistrictCreateRequest;
import com.xuancong.employee_management.dto.district.DistrictResponse;
import com.xuancong.employee_management.dto.paging.PageResponse;
import com.xuancong.employee_management.service.DistrictService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class DistrictController {
    private final DistrictService districtService;


    // pass
    @GetMapping({"/management/districts/{provinceId}","/customer/districts/{provinceId}"})
    public ResponseEntity<List<DistrictResponse>>  getDistrictByProvinceId(@PathVariable long provinceId){
        List<DistrictResponse> districts = districtService.getDistrictByProvinceId(provinceId);
        return ResponseEntity.ok(districts);
    }



    // pass
    @PostMapping("/management/districts")
    public ResponseEntity<DistrictResponse> createDistrict(
            @Valid @RequestBody DistrictCreateRequest districtCreateRequest
            ){
        return  ResponseEntity.ok(
                districtService.createDistrict(districtCreateRequest)
        );
    }


    // pass
    @GetMapping("/management/districts")
    public ResponseEntity<PageResponse<DistrictResponse>> getDistricts(
            @RequestParam(name = "keyword",defaultValue = "" ,required = false) String keyword,
            @RequestParam(name = "pageSize", defaultValue = Constants.Paging.DEFAULT_PAGE_SIZE,required = false) int pageSize,
            @RequestParam(name = "pageIndex",defaultValue = Constants.Paging.DEFAULT_PAGE_NUMBER,required = false) int pageIndex,
            @RequestParam(value = "countryId",required = false) final Long countryId,
            @RequestParam(name = "provinceId",required = false) final Long provinceId
    ){
        return  ResponseEntity.ok(
                districtService.getDistrict(keyword,pageIndex,pageSize,countryId,provinceId)
        );
    }




}
