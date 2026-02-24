package com.xuancong.employee_management.service;

import com.xuancong.employee_management.constants.Constants;
import com.xuancong.employee_management.dto.position.PositionCreateRequest;
import com.xuancong.employee_management.dto.position.PositionGetResponse;
import com.xuancong.employee_management.exception.DuplicateResourceException;
import com.xuancong.employee_management.model.Position;
import com.xuancong.employee_management.repository.PositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class PositionService {
    private final PositionRepository positionRepository;


    public PositionGetResponse createPosition(PositionCreateRequest positionCreateRequest) {
        this.validatePositionName(null,positionCreateRequest.name());
        Position position = positionCreateRequest.toPosition();
        this.positionRepository.save(position);
        return  PositionGetResponse.fromPosition(position);

    }

    private void validatePositionName(Long id,String name){
        if(this.checkExistedPositionName(id,name)){
            throw new DuplicateResourceException(Constants.ErrorKey.POSITION_NAME_EXITED,name);
        }
    }
    private boolean checkExistedPositionName(Long id,String name){
        return positionRepository.existsByNameIgnoreCaseAndIdNot(name,id);
    }



}
