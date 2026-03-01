package com.xuancong.employee_management.service;

import com.xuancong.employee_management.constants.Constants;
import com.xuancong.employee_management.dto.department.DepartmentCreateRequest;
import com.xuancong.employee_management.dto.department.DepartmentGetResponse;
import com.xuancong.employee_management.dto.department.DepartmentPagingGetResponse;
import com.xuancong.employee_management.exception.DuplicateResourceException;
import com.xuancong.employee_management.exception.NotFoundException;
import com.xuancong.employee_management.exception.ResourceInUseException;
import com.xuancong.employee_management.model.Department;
import com.xuancong.employee_management.repository.DepartmentRepository;
import com.xuancong.employee_management.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.apache.bcel.classfile.Constant;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    @CacheEvict(value = "departments", allEntries = true)
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

    @CacheEvict(value = "departments", allEntries = true)
    public void delete(Long id){
        Department department = this.validateExitedDepartment(id,departmentRepository,Constants.ErrorKey.DEPARTMENT_NOTFOUND);
        if(employeeRepository.existsByDepartment_Id(department.getId())){
            throw new ResourceInUseException(Constants.ErrorKey.DEPARTMENT_CONSTRAINT, department.getId());
        }
        this.departmentRepository.delete(department);

    }

    @Cacheable(
            value = "departments",
            key = "#page + ':' + #size + ':' + #name"
    )
    public DepartmentPagingGetResponse getDepartments(int page,int size,String name) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Department> departmentPage = departmentRepository.findByNameContainingIgnoreCase(name, pageable);
        List<DepartmentGetResponse> content = departmentPage.getContent().stream().map(DepartmentGetResponse::fromDepartment)
                .toList();
        return new DepartmentPagingGetResponse(
                content,
                (int) departmentPage.getTotalElements(),
                departmentPage.getTotalPages(),
                departmentPage.isLast()
        );

    }
}
