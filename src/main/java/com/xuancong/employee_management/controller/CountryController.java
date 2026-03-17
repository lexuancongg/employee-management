package com.xuancong.employee_management.controller;


import com.xuancong.employee_management.constants.Constants;
import com.xuancong.employee_management.dto.country.CountryCreateRequest;
import com.xuancong.employee_management.dto.country.CountryResponse;
import com.xuancong.employee_management.dto.paging.PageResponse;
import com.xuancong.employee_management.service.CountryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CountryController {

    private final CountryService countryService;
    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }



    @GetMapping("/countries")
    public ResponseEntity<PageResponse<CountryResponse>> getCountriesPaging(
            @RequestParam(name ="pageIndex" ,defaultValue = Constants.Paging.DEFAULT_PAGE_NUMBER,required = false)
            final int pageIndex,
            @RequestParam(name = "pageSize" ,defaultValue = Constants.Paging.DEFAULT_PAGE_SIZE,required = false)
            final int pageSize
    ){
        return ResponseEntity.ok(countryService.getCountriesPaging(pageIndex,pageSize));
    }


    // checked
    @GetMapping({"/management/countries","/customer/countries"})
    public ResponseEntity<List<CountryResponse>> getCountries(){
        return ResponseEntity.ok(countryService.getCountries());
    }

    // checked
    @PostMapping("/countries")
    public ResponseEntity<CountryResponse> createCountry(
            @RequestBody @Valid final CountryCreateRequest countryCreateRequest,
            final UriComponentsBuilder uriComponentsBuilder
            ){
        CountryResponse countrySaved = countryService.createCountry(countryCreateRequest);
        return ResponseEntity.created(
                        uriComponentsBuilder
                                .replacePath("/management/countries/{id}")
                                .buildAndExpand(countrySaved.id())
                                .toUri())
                .body(countrySaved);
    }

    // checked

    @PutMapping("/management/countries/{id}")
    public ResponseEntity<Void> updateCountry(@PathVariable Long id,
                                              @RequestBody @Valid final CountryCreateRequest countryCreateRequest){
        countryService.updateCountry(id, countryCreateRequest);
        return ResponseEntity.noContent().build();
    }


    // checked
    @DeleteMapping("/countries/{id}")
    public ResponseEntity<Void> deleteCountry(@PathVariable Long id){
        countryService.deleteCountry(id);
        return ResponseEntity.noContent().build();
    }


}
