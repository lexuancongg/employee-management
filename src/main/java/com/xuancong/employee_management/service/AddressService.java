package com.xuancong.employee_management.service;


import com.xuancong.employee_management.constants.Constants;
import com.xuancong.employee_management.dto.address.AddressCreateRequest;
import com.xuancong.employee_management.dto.address.AddressResponse;
import com.xuancong.employee_management.exception.NotFoundException;
import com.xuancong.employee_management.model.Address;
import com.xuancong.employee_management.repository.AddressRepository;
import com.xuancong.employee_management.repository.CountryRepository;
import com.xuancong.employee_management.repository.DistrictRepository;
import com.xuancong.employee_management.repository.ProvinceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;
    private final CountryRepository countryRepository;
    private final ProvinceRepository provinceRepository;
    private final DistrictRepository districtRepository;

    public AddressResponse createAddress(AddressCreateRequest addressCreateRequest){
        Address address = new Address();
        address.setSpecificAddress(addressCreateRequest.specificAddress());
        this.performSetEntityIfExistsOrThrow(address, addressCreateRequest);
        return AddressResponse.fromAddress(addressRepository.save(address));
    }

    

    public void updateAddress(Long id, AddressCreateRequest addressCreateRequest){
        Address address = addressRepository.findById(id)
                .orElseThrow(() ->  new NotFoundException(Constants.ErrorKey.ADDRESS_NOT_FOUND,id));
        this.performSetEntityIfExistsOrThrow(address, addressCreateRequest);
        address.setSpecificAddress(addressCreateRequest.specificAddress());

        addressRepository.save(address);
    }

    private void performSetEntityIfExistsOrThrow(Address address, AddressCreateRequest addressCreateRequest){
        this.setEntityIfExistsOrThrow(
                addressCreateRequest.countryId(),
                countryRepository::findById ,
                Constants.ErrorKey.COUNTRY_NOT_FOUND,
                address::setCountry
        );
        this.setEntityIfExistsOrThrow(
                addressCreateRequest.provinceId(),
                provinceRepository::findById,
                Constants.ErrorKey.PROVINCE_NOT_FOUND ,
                address::setProvince
        );
        this.setEntityIfExistsOrThrow(addressCreateRequest.districtId(),districtRepository::findById,
                Constants.ErrorKey.DISTRICT_NOT_FOUND, address::setDistrict);
    }


    public <E> void setEntityIfExistsOrThrow(Long id, Function<Long, Optional<E>> repositoryFindById ,
                                             String errorKey , Consumer<E>  setter){
        Optional<E> optional = repositoryFindById.apply(id);
        if(optional.isEmpty()){
            throw  new NotFoundException(errorKey, id);
        }
        setter.accept(optional.get());

    }







}
