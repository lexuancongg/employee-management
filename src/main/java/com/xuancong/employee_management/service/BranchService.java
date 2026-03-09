package com.xuancong.employee_management.service;

import com.xuancong.employee_management.dto.branch.BranchCreateRequest;
import com.xuancong.employee_management.dto.branch.BranchResponse;
import com.xuancong.employee_management.model.Branch;
import com.xuancong.employee_management.repository.BranchRepository;
import com.xuancong.employee_management.repository.CountryRepository;
import com.xuancong.employee_management.repository.DistrictRepository;
import com.xuancong.employee_management.repository.ProvinceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BranchService {
    private final BranchRepository branchRepository;
    private final CountryRepository countryRepository;
    private final ProvinceRepository provinceRepository;
    private final DistrictRepository districtRepository;
    public BranchResponse create(BranchCreateRequest branchCreateRequest){
        Branch branch = new Branch();
        branch.setName(branchCreateRequest.name());

    }
}
