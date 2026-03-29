package com.xuancong.employee_management.service;

import com.xuancong.employee_management.constants.Constants;
import com.xuancong.employee_management.dto.branch.BranchResponse;
import com.xuancong.employee_management.dto.district.DistrictCreateRequest;
import com.xuancong.employee_management.dto.district.DistrictResponse;
import com.xuancong.employee_management.dto.paging.PageResponse;
import com.xuancong.employee_management.dto.province.ProvinceCreateRequest;
import com.xuancong.employee_management.exception.DuplicateResourceException;
import com.xuancong.employee_management.exception.NotFoundException;
import com.xuancong.employee_management.model.District;
import com.xuancong.employee_management.repository.DistrictRepository;
import com.xuancong.employee_management.repository.ProvinceRepository;
import com.xuancong.employee_management.specification.DistrictSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DistrictService {
    private final DistrictRepository districtRepository;
    private final ProvinceRepository provinceRepository;

    public DistrictService(DistrictRepository districtRepository, ProvinceRepository provinceRepository) {
        this.districtRepository = districtRepository;
        this.provinceRepository = provinceRepository;
    }

    public List<DistrictResponse> getDistrictByProvinceId(Long provinceId) {
        // check provinceId
        boolean provinceExits = provinceRepository.existsById(provinceId);
        if (!provinceExits) {
           throw new NotFoundException(Constants.ErrorKey.PROVINCE_NOT_FOUND,provinceId);
        }
        List<District> districts = districtRepository.findAllWithProvinceAndCountryByProvinceId(provinceId);
        return districts.stream()
                .map(DistrictResponse::fromDistrict)
                .toList();
    }

    public DistrictResponse createDistrict(DistrictCreateRequest districtCreateRequest){
        District district = new District();
        Long provinceId  = districtCreateRequest.provinceId();
        boolean provinceExits = provinceRepository.existsById(provinceId);
        if (!provinceExits) {
            throw new NotFoundException(Constants.ErrorKey.PROVINCE_NOT_FOUND,provinceId);

        }
        this.validateDuplicateName(districtCreateRequest,null);
        district.setName(districtCreateRequest.name());
        district.setProvince(provinceRepository.getReferenceById(provinceId));
        return  DistrictResponse.fromDistrict(districtRepository.save(district));
    }
    private void validateDuplicateName(DistrictCreateRequest districtCreateRequest, Long excludeId ){
        if(this.isDuplicateName(districtCreateRequest,excludeId)){
            throw  new DuplicateResourceException(Constants.ErrorKey.NAME_DUPLICATE,
                    districtCreateRequest.name());
        }
    }
    private boolean isDuplicateName(DistrictCreateRequest districtCreateRequest, Long excludeId){
        return this.districtRepository.existsByNameIgnoreCaseAndProvince_IdAndIdNot(
                districtCreateRequest.name(),
                districtCreateRequest.provinceId(),
                excludeId
        );
    }

    public PageResponse<DistrictResponse> getDistrict(String keyword,int pageIndex,int pageSize,Long countryId,Long provinceId){
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        Page<District>  districtPage = districtRepository.findAll(
                DistrictSpecification.getDistrictByFilter(keyword,countryId,provinceId),pageable
        );
        List<DistrictResponse> payload = districtPage.getContent()
                .stream()
                .map(DistrictResponse::fromDistrict)
                .toList();
        return PageResponse.<DistrictResponse>builder()
                .content(payload)
                .isLast(districtPage.isLast())
                .totalElements(districtPage.getTotalPages())
                .build();

    }
}
