package com.xuancong.employee_management.service;

import com.xuancong.employee_management.constants.Constants;
import com.xuancong.employee_management.dto.employee.EmployeeCreateRequest;
import com.xuancong.employee_management.dto.employee.EmployeeGetResponse;
import com.xuancong.employee_management.exception.NotFoundException;
import com.xuancong.employee_management.model.Employee;
import com.xuancong.employee_management.model.User;
import com.xuancong.employee_management.repository.DepartmentRepository;
import com.xuancong.employee_management.repository.EmployeeRepository;
import com.xuancong.employee_management.repository.PositionRepository;
import com.xuancong.employee_management.repository.UserRepository;
import com.xuancong.employee_management.utils.PasswordUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.lang.model.element.NestingKind;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final PositionRepository positionRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final MailService mailService;
    private final DepartmentRepository departmentRepository;
    public EmployeeGetResponse createEmployee(EmployeeCreateRequest employeeCreateRequest) {
        Employee employee = employeeCreateRequest.toBaseEmployee();
        String employeeCode = this.generateEmployeeCode();
        employee.setEmployeeCode(employeeCode);
        this.setEntityIfExistsOrThrow(employeeCreateRequest.positionId(), positionRepository::findById ,
                Constants.ErrorKey.POSITION_NOT_FOUND,employee::setPosition);
        this.setEntityIfExistsOrThrow(employeeCreateRequest.departmentId(),departmentRepository::findById,
                Constants.ErrorKey.DEPARTMENT_NOT_FOUND,employee::setDepartment);


        // tạo user
        String rawPassword = this.genericAccount(employeeCreateRequest, employeeCode,employee);
        employeeRepository.save(employee);
        this.sendEmail(employeeCreateRequest,employeeCode,rawPassword);
        return EmployeeGetResponse.fromEmployee(employee);

    }

    // có thể trùng code nếu hai req gởi cùng lúc=> chạy song song hai thread => fix bằng SEQUENCE
    private String generateEmployeeCode() {
        String maxCode = employeeRepository.findMaxEmployeeCode();

        if (maxCode == null) {
            return "EMP0001";
        }

        int number = Integer.parseInt(maxCode.substring(3));
        return String.format("EMP%04d", number + 1);
    }
    private <E> void setEntityIfExistsOrThrow(Long id, Function<Long, Optional<E>> finderById , String errorKey , Consumer<E> setter){
        Optional<E> optional = finderById.apply(id);
        if(optional.isEmpty()){
            throw  new NotFoundException(errorKey);
        }
        setter.accept(optional.get());

    }
    private String genericAccount(EmployeeCreateRequest employeeCreateRequest,  String employeeCode, Employee employee){
        String rawPassword = PasswordUtils.generateRandomPassword(10);
        String encoded = passwordEncoder.encode(rawPassword);
        User user = new User();
        user.setEmail(employeeCreateRequest.email());
        user.setPassword(encoded);
        user.setUsername(employeeCode);
        user.setPhone(employeeCreateRequest.phone());
        user.setEnabled(true);
        userRepository.save(user);
        employee.setUser(user);
        return rawPassword;
    }

    private void sendEmail(EmployeeCreateRequest employeeCreateRequest ,String employeeCode,String rawPassword) {
        this.mailService.sendAccountEmail(employeeCreateRequest.email(),employeeCode,rawPassword);
    }
}
