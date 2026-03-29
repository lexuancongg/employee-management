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



    // pass
    @GetMapping("/management/countries")
    public ResponseEntity<PageResponse<CountryResponse>> getCountriesPaging(
            @RequestParam(name ="pageIndex" ,defaultValue = Constants.Paging.DEFAULT_PAGE_NUMBER,required = false)
            final int pageIndex,
            @RequestParam(name = "pageSize" ,defaultValue = Constants.Paging.DEFAULT_PAGE_SIZE,required = false)
            final int pageSize,
            @RequestParam(name = "keyword",defaultValue = "" ,required = false) String keyword
            ){
        return ResponseEntity.ok(countryService.getCountriesPaging(pageIndex,pageSize,keyword));
    }



    // pass
    @GetMapping({"/employee/countries"})
    public ResponseEntity<List<CountryResponse>> getCountries(){
        return ResponseEntity.ok(countryService.getCountries());
    }




    // pass
    @PostMapping("/management/countries")
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


    // pass
    @PutMapping("/management/countries/{id}")
    public ResponseEntity<Void> updateCountry(@PathVariable Long id,
                                              @RequestBody @Valid final CountryCreateRequest countryCreateRequest){
        countryService.updateCountry(id, countryCreateRequest);
        return ResponseEntity.noContent().build();
    }


    // pass
    @DeleteMapping("/management/countries/{id}")
    public ResponseEntity<Void> deleteCountry(@PathVariable Long id){
        countryService.deleteCountry(id);
        return ResponseEntity.noContent().build();
    }


}
