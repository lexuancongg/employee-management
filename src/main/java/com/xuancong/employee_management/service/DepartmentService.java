package com.xuancong.employee_management.service;

import com.xuancong.employee_management.constants.Constants;
import com.xuancong.employee_management.dto.department.DepartmentCreateRequest;
import com.xuancong.employee_management.dto.department.DepartmentGetResponse;
import com.xuancong.employee_management.exception.DuplicateResourceException;
import com.xuancong.employee_management.exception.NotFoundException;
import com.xuancong.employee_management.model.Department;
import com.xuancong.employee_management.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.apache.bcel.classfile.Constant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    public DepartmentGetResponse createDepartment(DepartmentCreateRequest departmentCreateRequest){
        this.validateDepartmentName(departmentCreateRequest.name(),null);
        Department department = departmentCreateRequest.toDepartment();
        this.departmentRepository.save(department);
        return  DepartmentGetResponse.fromDepartment(department);

    }
    private void validateDepartmentName(String name,Long id){
        if(this.checkExistedDepartmentName(name,id)){
            throw new DuplicateResourceException(Constants.ErrorKey.DEPARTMENT_DUPLICATE, name);
        }
    }
    private boolean  checkExistedDepartmentName(String name,Long id){
        return  this.departmentRepository.existsByNameIgnoreCaseAndIdNot(name,id);
    }

    public void updateDepartment(Long id, DepartmentCreateRequest departmentCreateRequest){
         Department department =
                 this.validateExitedDepartment(id,departmentRepository,Constants.ErrorKey.DEPARTMENT_NOTFOUND);
        this.validateDepartmentName(departmentCreateRequest.name(),id);
        department.setName(departmentCreateRequest.name());
        this.departmentRepository.save(department);

    }

    private <E,T> T validateExitedDepartment(E id, JpaRepository<T,E> finder, String errorKey) {
        return finder.findById(id)
                .orElseThrow(() -> new NotFoundException(errorKey,id));

    }
}
