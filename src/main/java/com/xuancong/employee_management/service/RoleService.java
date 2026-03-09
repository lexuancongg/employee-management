package com.xuancong.employee_management.service;

import com.xuancong.employee_management.constants.Constants;
import com.xuancong.employee_management.dto.role.RoleCreateRequest;
import com.xuancong.employee_management.dto.role.RoleResponse;
import com.xuancong.employee_management.exception.DuplicateResourceException;
import com.xuancong.employee_management.exception.NotFoundException;
import com.xuancong.employee_management.exception.ResourceInUseException;
import com.xuancong.employee_management.model.Role;
import com.xuancong.employee_management.repository.RoleRepository;
import com.xuancong.employee_management.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.apache.bcel.classfile.Constant;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;


    public RoleResponse create(RoleCreateRequest roleCreateRequest){
        Role role = roleCreateRequest.toRole();
        this.validateDuplicateName(roleCreateRequest.name(), null);
        roleRepository.save(role);
        return RoleResponse.fromRole(role);
    }

    private void validateDuplicateName(String name,Long id ){
        if(roleRepository.existsByNameIgnoreCaseAndIdNot(name, id)){
            throw new  DuplicateResourceException(Constants.ErrorKey.NAME_DUPLICATE,name);
        }

    }


    public void updateRole(Long id, RoleCreateRequest roleCreateRequest){
        Role role = this.getRoleOrThrow(id);
        this.validateDuplicateName(roleCreateRequest.name(), role.getId());
        role.setName(roleCreateRequest.name());
        roleRepository.save(role);

    }

    private Role getRoleOrThrow(Long id){
        return  roleRepository.findById(id)
                .orElseThrow(()-> new NotFoundException(Constants.ErrorKey.ROLE_NOT_FOUND));
    }


    public void deleteRole(Long id){
        Role role = this.getRoleOrThrow(id);
        if(userRoleRepository.existsByRole(role)){
            throw  new ResourceInUseException("");
        }
        roleRepository.delete(role);

    }

}
