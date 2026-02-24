package com.xuancong.employee_management.service;

import com.xuancong.employee_management.constants.Constants;
import com.xuancong.employee_management.dto.position.PositionCreateRequest;
import com.xuancong.employee_management.dto.position.PositionGetResponse;
import com.xuancong.employee_management.dto.position.PositionPagingGetResponse;
import com.xuancong.employee_management.exception.DuplicateResourceException;
import com.xuancong.employee_management.exception.NotFoundException;
import com.xuancong.employee_management.exception.ResourceInUseException;
import com.xuancong.employee_management.model.Position;
import com.xuancong.employee_management.repository.EmployeeRepository;
import com.xuancong.employee_management.repository.PositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class PositionService {
    private final PositionRepository positionRepository;

    private final EmployeeRepository employeeRepository;

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

    public PositionPagingGetResponse getPositions(int pageIndex,int pageSize,String keyword){
        Pageable pageable = PageRequest.of(pageIndex,pageSize);
        Page<Position> positionPage = positionRepository.findAllByNameContainingIgnoreCase(keyword,pageable);
        List<PositionGetResponse> content = positionPage.getContent().stream()
                .map(PositionGetResponse::fromPosition)
                .toList();

        return new PositionPagingGetResponse(
                content,
                (int) positionPage.getTotalElements(),
                positionPage.getTotalPages(),
                positionPage.isLast()
        );
    }


    public PositionGetResponse getPositionById(Long id){
        Position position = positionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Constants.ErrorKey.POSITION_NOT_FOUND,id));
        return PositionGetResponse.fromPosition(position);
    }


    public void updatePosition(Long id,PositionCreateRequest positionCreateRequest){
        Position position = this.positionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Constants.ErrorKey.POSITION_NOT_FOUND,id));
        this.validatePositionName(id,positionCreateRequest.name());
        position.setName(positionCreateRequest.name());
        this.positionRepository.save(position);

    }

    public void deletePosition(Long id){
        Position position = positionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Constants.ErrorKey.POSITION_NOT_FOUND,id));
        // chậm hơn nếu dữ liệu lớn
//        if(!position.getEmployees().isEmpty()){
//            throw
//        }

        if(employeeRepository.existsByPosition_Id(id)){
            throw new ResourceInUseException(Constants.ErrorKey.POSITION_IN_USE,id);
        }

        this.positionRepository.delete(position);

    }


}
