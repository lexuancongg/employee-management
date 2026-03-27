package com.xuancong.employee_management.service;

import com.xuancong.employee_management.constants.Constants;
import com.xuancong.employee_management.dto.paging.PageResponse;
import com.xuancong.employee_management.dto.employee.EmployeeCreateRequest;
import com.xuancong.employee_management.dto.employee.EmployeeDetailResponse;
import com.xuancong.employee_management.dto.employee.EmployeeResponse;
import com.xuancong.employee_management.enums.EmployeeStatus;
import com.xuancong.employee_management.exception.DuplicateResourceException;
import com.xuancong.employee_management.exception.NotFoundException;
import com.xuancong.employee_management.kafka.message.EmployCreatedMessage;
import com.xuancong.employee_management.model.Employee;
import com.xuancong.employee_management.model.User;
import com.xuancong.employee_management.repository.DepartmentRepository;
import com.xuancong.employee_management.repository.EmployeeRepository;
import com.xuancong.employee_management.repository.PositionRepository;
import com.xuancong.employee_management.repository.UserRepository;
import com.xuancong.employee_management.specification.EmployeeSpecification;
import com.xuancong.employee_management.utils.PasswordUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
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
    private final KafkaTemplate<String, EmployCreatedMessage> kafkaTemplate;


    public EmployeeResponse createEmployee(EmployeeCreateRequest employeeCreateRequest) {
        this.validateEmployeeInfo(employeeCreateRequest, null);
        Employee employee = employeeCreateRequest.toBaseEmployee();
        String employeeCode = this.generateEmployeeCode();
        employee.setEmployeeCode(employeeCode);
        this.setReferenceOrThrow(employeeCreateRequest, employee);
        // tạo user
        String password = this.genericAccount(employeeCreateRequest, employeeCode, employee);
        employeeRepository.save(employee);
        mailService.sendAccountEmail(employeeCreateRequest.email(),employeeCode,password);
        this.publishEmployeeCreatedEvent(employeeCreateRequest.email(), employeeCode, password);
        return EmployeeResponse.fromEmployee(employee);

    }

    private void publishEmployeeCreatedEvent(String email, String code, String password) {
        EmployCreatedMessage message = EmployCreatedMessage.builder()
                .email(email)
                .password(password)
                .username(code)
                .build();
        kafkaTemplate.send("employee-created-topic", message)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        System.out.println(" Send failed: " + ex.getMessage());
                    } else {
                        System.out.println("Sent to partition: "
                                + result.getRecordMetadata().partition()
                                + ", offset: " + result.getRecordMetadata().offset());
                    }
                });
    }

    private void setReferenceOrThrow(EmployeeCreateRequest employeeCreateRequest, Employee employee) {
        this.setEntityIfExistsOrThrow(employeeCreateRequest.positionId(), positionRepository::findById,
                Constants.ErrorKey.POSITION_NOT_FOUND, employee::setPosition);
        this.setEntityIfExistsOrThrow(employeeCreateRequest.departmentId(), departmentRepository::findById,
                Constants.ErrorKey.DEPARTMENT_NOT_FOUND, employee::setDepartment);

    }

    private void validateEmployeeInfo(EmployeeCreateRequest employeeCreateRequest, Long employeeId) {
        this.validateUnique(employeeCreateRequest, employeeId);
    }

    private void validateUnique(EmployeeCreateRequest employeeCreateRequest, Long employeeId) {
        this.validateUniqueProperty(employeeId, employeeRepository::existsByPhoneAndIdNot,
                employeeCreateRequest.phone(), Constants.ErrorKey.PHONE_ALREADY_EXISTS);
        this.validateUniqueProperty(employeeId, employeeRepository::existsByEmailAndIdNot,
                employeeCreateRequest.email(), Constants.ErrorKey.EMAIL_ALREADY_EXISTS);
    }

    private <T, E> void validateUniqueProperty(E id, BiFunction<T, E, Boolean> finder, T property, String errorKey) {
        if (finder.apply(property, id)) {
            throw new DuplicateResourceException(errorKey, property);
        }
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

    private <E> void setEntityIfExistsOrThrow(Long id, Function<Long, Optional<E>> finderById, String errorKey, Consumer<E> setter) {
        Optional<E> optional = finderById.apply(id);
        if (optional.isEmpty()) {
            throw new NotFoundException(errorKey);
        }
        setter.accept(optional.get());

    }

    private String genericAccount(EmployeeCreateRequest employeeCreateRequest, String employeeCode, Employee employee) {
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


    public void updateEmployee(Long id, EmployeeCreateRequest employeeCreateRequest) {
        Employee employee = this.validateExitedEmployee(id, employeeRepository, Constants.ErrorKey.EMPLOYEE_NOT_FOUND);
        this.validateEmployeeInfo(employeeCreateRequest, id);
        this.updateFieldEmployee(employeeCreateRequest, employee);
        employeeRepository.save(employee);
    }

    private <E, T> T validateExitedEmployee(E id, JpaRepository<T, E> finder, String errorKey) {
        return finder.findById(id)
                .orElseThrow(() -> new NotFoundException(errorKey, id));

    }

    public void updateFieldEmployee(EmployeeCreateRequest employeeCreateRequest, Employee employee) {
        employee.setPhone(employeeCreateRequest.phone());
        employee.setEmail(employeeCreateRequest.email());
        employee.setName(employeeCreateRequest.name());
        employee.setGender(employeeCreateRequest.gender());
        employee.setBirthday(employeeCreateRequest.birthday());
        employee.setHireDate(employeeCreateRequest.hireDate());
        employee.setStatus(employeeCreateRequest.status());
        employee.setAvatarId(employeeCreateRequest.avatarId());
        this.setReferenceOrThrow(employeeCreateRequest, employee);

    }

    public EmployeeDetailResponse getEmployee(Long id) {
        Employee employee = this.validateExitedEmployee(id, employeeRepository, Constants.ErrorKey.EMPLOYEE_NOT_FOUND);
        Long avatarId = employee.getAvatarId();
        // lấy url ,
        return EmployeeDetailResponse.from(employee);

    }

    public PageResponse<EmployeeResponse> getEmployees(
            String name,
            String code,
            String email,
            Long departmentId,
            Long branchId,
            Long positionId,
            EmployeeStatus status,
            LocalDate hireDateFrom,
            LocalDate hireDateTo,
            int page,
            int size,
            String sortBy,
            String sortDir
    )  {


        Sort sort = Sort.by(sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<Employee> specification = EmployeeSpecification.filter(
                name, code, email, departmentId, branchId, positionId, status, hireDateFrom, hireDateTo
        );
        Page<Employee> employeePage = this.employeeRepository.findAll(specification, pageable);
        List<Employee> employees = employeePage.getContent();
        List<EmployeeResponse> content = employees.stream()
                .map(EmployeeResponse::fromEmployee)
                .toList();

        return new PageResponse<EmployeeResponse>(
                content,
                (int) employeePage.getTotalElements(),
                employeePage.getTotalPages(),
                employeePage.isLast()
        );

    }

    public void deleteEmployee(Long id) {
        Employee employee = this.validateExitedEmployee(id, employeeRepository, Constants.ErrorKey.EMPLOYEE_NOT_FOUND);

        employeeRepository.delete(employee);

    }
}