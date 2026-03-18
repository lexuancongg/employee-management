package com.xuancong.employee_management.controller;


import com.xuancong.employee_management.constants.Constants;
import com.xuancong.employee_management.dto.paging.PageResponse;
import com.xuancong.employee_management.dto.province.ProvinceCreateRequest;
import com.xuancong.employee_management.dto.province.ProvinceResponse;
import com.xuancong.employee_management.service.ProvinceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProvinceController {
    private final ProvinceService provinceService;
    public ProvinceController(ProvinceService provinceService) {
        this.provinceService = provinceService;
    }


    @GetMapping("/provinces")
    public ResponseEntity<PageResponse<ProvinceResponse>> getProvincesPaging(
            @RequestParam(value = "pageIndex",defaultValue = Constants.Paging.DEFAULT_PAGE_NUMBER,required = false)
            final int pageIndex,
            @RequestParam(value = "pageSize",defaultValue = Constants.Paging.DEFAULT_PAGE_SIZE,required = false)
            final  int pageSize,
            @RequestParam(value = "countryId",required = false) final Long countryId
    ) {
        PageResponse<ProvinceResponse> provincePaging =
                provinceService.getProvincesPaging(pageIndex,pageSize,countryId);
        return ResponseEntity.ok(provincePaging);

    }



    @GetMapping({"/management/provinces/{countryId}","/customer/provinces/{countryId}"})
    public ResponseEntity<List<ProvinceResponse>> getProvincesByCountryId(
            @PathVariable(value = "countryId") final Long countryId
    ) {
        List<ProvinceResponse> provinces = provinceService.getProvincesByCountryId(countryId);
        return ResponseEntity.ok(provinces);
    }



    @PostMapping("/management/provinces")
    public ResponseEntity<ProvinceResponse> createProvince(
            @RequestBody @Valid final ProvinceCreateRequest provinceCreateRequest
            ){
        ProvinceResponse provinceSaved = provinceService.createProvince(provinceCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(provinceSaved);
    }



    @PutMapping("/management/provinces/{id}")
    public ResponseEntity<Void> updateProvince(@PathVariable Long id ,
                                               @RequestBody @Valid ProvinceCreateRequest provinceCreateRequest){
        provinceService.updateProvince(id, provinceCreateRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/management/provinces/{id}")
    public ResponseEntity<Void> deleteProvince(@PathVariable Long id){
        provinceService.deleteProvince(id);
        return ResponseEntity.noContent().build();
    }

}
