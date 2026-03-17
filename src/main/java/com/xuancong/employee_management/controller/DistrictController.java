package com.xuancong.employee_management.controller;

import com.xuancong.employee_management.dto.district.DistrictResponse;
import com.xuancong.employee_management.service.DistrictService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DistrictController {
    private final DistrictService districtService;


    @GetMapping({"/management/districts/{provinceId}","/customer/districts/{provinceId}"})
    public ResponseEntity<List<DistrictResponse>>  getDistrictByProvinceId(@PathVariable long provinceId){
        List<DistrictResponse> districts = districtService.getDistrictByProvinceId(provinceId);
        return ResponseEntity.ok(districts);
    }

}
