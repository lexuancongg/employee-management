package com.xuancong.employee_management.service;

import com.xuancong.employee_management.constants.Constants;
import com.xuancong.employee_management.dto.district.DistrictResponse;
import com.xuancong.employee_management.exception.NotFoundException;
import com.xuancong.employee_management.model.District;
import com.xuancong.employee_management.repository.DistrictRepository;
import com.xuancong.employee_management.repository.ProvinceRepository;
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
        List<District> districts = districtRepository.findAllByProvince_Id(provinceId);
        return districts.stream()
                .map(DistrictResponse::fromDistrict)
                .toList();
    }
}
