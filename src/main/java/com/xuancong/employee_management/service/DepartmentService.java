package com.xuancong.employee_management.service;

import com.xuancong.employee_management.constants.Constants;
import com.xuancong.employee_management.dto.paging.PageResponse;
import com.xuancong.employee_management.dto.department.DepartmentCreateRequest;
import com.xuancong.employee_management.dto.department.DepartmentResponse;
import com.xuancong.employee_management.exception.DuplicateResourceException;
import com.xuancong.employee_management.exception.NotFoundException;
import com.xuancong.employee_management.exception.ResourceInUseException;
import com.xuancong.employee_management.model.Branch;
import com.xuancong.employee_management.model.Department;
import com.xuancong.employee_management.repository.BranchRepository;
import com.xuancong.employee_management.repository.DepartmentRepository;
import com.xuancong.employee_management.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final BranchRepository branchRepository;

    @CacheEvict(value = "departments", allEntries = true)
    public DepartmentResponse createDepartment(DepartmentCreateRequest departmentCreateRequest) {
        Long branchId = departmentCreateRequest.branchId();
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new NotFoundException(Constants.ErrorKey.BRANCH_NOT_FOUND));

        this.validateDepartmentName(departmentCreateRequest.name(), null, branchId);
        Department department = departmentCreateRequest.toDepartment(branch);
        this.departmentRepository.save(department);
        return DepartmentResponse.fromDepartment(department);

    }

    private void validateDepartmentName(String name, Long id, Long branchId) {
        if (this.checkExistedDepartmentName(name, branchId, id)) {
            throw new DuplicateResourceException(Constants.ErrorKey.NAME_DUPLICATE, name);
        }
    }

    private boolean checkExistedDepartmentName(String name, Long branchId, Long id) {
        return this.departmentRepository.existsByNameIgnoreCaseAndBranch_IdAndIdNot(name, branchId, id);
    }

    public void updateDepartment(Long id, DepartmentCreateRequest departmentCreateRequest) {
        Long branchId = departmentCreateRequest.branchId();
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new NotFoundException(Constants.ErrorKey.BRANCH_NOT_FOUND));

        Department department =
                this.validateExitedDepartment(id, departmentRepository, Constants.ErrorKey.DEPARTMENT_NOT_FOUND);
        this.validateDepartmentName(departmentCreateRequest.name(), id, branchId);
        department.setName(departmentCreateRequest.name());
        department.setBranch(branch);
        this.departmentRepository.save(department);

    }

    private <E, T> T validateExitedDepartment(E id, JpaRepository<T, E> finder, String errorKey) {
        return finder.findById(id)
                .orElseThrow(() -> new NotFoundException(errorKey, id));

    }

    @CacheEvict(value = "departments", allEntries = true)
    public void delete(Long id) {
        Department department = this.validateExitedDepartment(id, departmentRepository, Constants.ErrorKey.DEPARTMENT_NOT_FOUND);
        if (employeeRepository.existsByDepartment_Id(department.getId())) {
            throw new ResourceInUseException(Constants.ErrorKey.DEPARTMENT_HAS_EMPLOYEES, department.getId());
        }
        this.departmentRepository.delete(department);

    }

    @Cacheable(
            value = "departments",
            key = "#page + ':' + #size + ':' + #name"
    )
    public PageResponse<DepartmentResponse> getDepartments(int page, int size, String name,Long branchId) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Department> departmentPage = departmentRepository.findByNameContainingIgnoreCaseAndBranch_Id(name,branchId,pageable);
        List<DepartmentResponse> content = departmentPage.getContent().stream().map(DepartmentResponse::fromDepartment)
                .toList();
        return new PageResponse<DepartmentResponse>(
                content,
                (int) departmentPage.getTotalElements(),
                departmentPage.getTotalPages(),
                departmentPage.isLast()
        );

    }
}
