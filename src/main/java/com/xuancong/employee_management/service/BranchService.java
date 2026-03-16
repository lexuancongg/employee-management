package com.xuancong.employee_management.service;

import com.xuancong.employee_management.constants.Constants;
import com.xuancong.employee_management.dto.address.AddressCreateRequest;
import com.xuancong.employee_management.dto.branch.BranchCreateRequest;
import com.xuancong.employee_management.dto.branch.BranchResponse;
import com.xuancong.employee_management.exception.NotFoundException;
import com.xuancong.employee_management.exception.ResourceInUseException;
import com.xuancong.employee_management.model.Address;
import com.xuancong.employee_management.model.Branch;
import com.xuancong.employee_management.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Transactional
public class BranchService {
    private final BranchRepository branchRepository;
    private final CountryRepository countryRepository;
    private final ProvinceRepository provinceRepository;
    private final DistrictRepository districtRepository;
    private final EmployeeRepository employeeRepository;

    public BranchResponse create(BranchCreateRequest branchCreateRequest){
        Branch branch = new Branch();
        branch.setName(branchCreateRequest.name());
        Address address = new Address();
        this.setAddress(address,branchCreateRequest.address());
        branch.setAddress(address);
        branchRepository.save(branch);
        return BranchResponse.fromBranch(branch);

    }




    private <T> T getEntityOrThrow(Long id,
                                   Function<Long, Optional<T>> finder,
                                   String errorKey) {

        return finder.apply(id)
                .orElseThrow(() -> new NotFoundException(errorKey, id));
    }


    public void delete(Long id){
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Constants.ErrorKey.BRANCH_NOT_FOUND));
//        if(employeeRepository.existsByBranch(branch)){
//            throw new ResourceInUseException(Constants.ErrorKey.BRANCH_HAS_EMPLOYEE);
//        }
        branchRepository.delete(branch);
    }
    private void setAddress(Address address, AddressCreateRequest req){

        address.setCountry(
                getEntityOrThrow(req.countryId(),
                        countryRepository::findById,
                        Constants.ErrorKey.COUNTRY_NOT_FOUND)
        );

        address.setProvince(
                getEntityOrThrow(req.provinceId(),
                        provinceRepository::findById,
                        Constants.ErrorKey.PROVINCE_NOT_FOUND)
        );

        address.setDistrict(
                getEntityOrThrow(req.districtId(),
                        districtRepository::findById,
                        Constants.ErrorKey.DISTRICT_NOT_FOUND)
        );

        address.setSpecificAddress(req.specificAddress());
    }


    public void update(Long id,BranchCreateRequest branchCreateRequest){
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Constants.ErrorKey.BRANCH_NOT_FOUND));
        branch.setName(branchCreateRequest.name());
        Address address = branch.getAddress();
        this.setAddress(address,branchCreateRequest.address());
        branch.setAddress(address);
        branchRepository.save(branch);


    }
}
