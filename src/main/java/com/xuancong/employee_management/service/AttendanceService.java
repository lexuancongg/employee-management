package com.xuancong.employee_management.service;

import com.xuancong.employee_management.constants.Constants;
import com.xuancong.employee_management.dto.attendance.AttendanceGetResponse;
import com.xuancong.employee_management.exception.DuplicateResourceException;
import com.xuancong.employee_management.exception.NotFoundException;
import com.xuancong.employee_management.model.Attendance;
import com.xuancong.employee_management.model.Employee;
import com.xuancong.employee_management.model.User;
import com.xuancong.employee_management.repository.AttendanceRepository;
import com.xuancong.employee_management.repository.EmployeeRepository;
import com.xuancong.employee_management.repository.UserRepository;
import com.xuancong.employee_management.utils.AuthenticationUtils;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;


    public AttendanceGetResponse checkIn(){
        Employee employee = this.getEmployee();
        LocalDate workDate = LocalDate.now();
        LocalDateTime timeCheckIn = LocalDateTime.now();
        this.validateDuplicateCheckIn(employee,workDate);
        Attendance attendance = Attendance.builder()
                .checkIn(timeCheckIn)
                .employee(employee)
                .workDate(workDate)
                .build();
        attendanceRepository.save(attendance);
        return AttendanceGetResponse.fromAttendance(attendance);
    }

    private void validateDuplicateCheckIn(Employee employee , LocalDate workDate){
        if(attendanceRepository.existsByEmployeeAndWorkDate(employee, workDate)){
            throw new DuplicateResourceException(Constants.ErrorKey.CHECKIN_DUPLICATE,workDate);
        }
    }

    private Employee getEmployee(){
        String username = AuthenticationUtils.extractUsername();
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if(optionalUser.isEmpty()){
            throw  new NotFoundException(Constants.ErrorKey.USERNAME_NOT_FOUND,username);
        }
        User user = optionalUser.get();
        return  employeeRepository.findByUser_Username(user.getUsername())
                .orElseThrow(() -> new NotFoundException(Constants.ErrorKey.EMPLOYEE_NOT_FOUND,username));

    }
}
