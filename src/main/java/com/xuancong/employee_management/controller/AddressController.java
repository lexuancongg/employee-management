package com.xuancong.employee_management.controller;

import com.xuancong.employee_management.dto.address.AddressCreateRequest;
import com.xuancong.employee_management.dto.address.AddressResponse;
import com.xuancong.employee_management.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;

    @PostMapping({"/addresses"})
    public ResponseEntity<AddressResponse> createAddress(
            @RequestBody @Valid AddressCreateRequest addressCreateRequest
    ){
        return ResponseEntity.ok(addressService.createAddress(addressCreateRequest));
    }




    @PutMapping({"/addresses/{id}"})
    public ResponseEntity<Void> updateAddress(@PathVariable Long id,
                                              @RequestBody @Valid AddressCreateRequest addressCreateRequest){
        addressService.updateAddress(id, addressCreateRequest);
        return ResponseEntity.ok().build();
    }




}
