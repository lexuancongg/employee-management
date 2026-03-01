package com.xuancong.employee_management.service;

import com.xuancong.employee_management.constants.Constants;
import com.xuancong.employee_management.dto.attendance.AttendanceGetResponse;
import com.xuancong.employee_management.dto.attendance.AttendancePagingGetResponse;
import com.xuancong.employee_management.dto.attendance.AttendanceStatusResponse;
import com.xuancong.employee_management.exception.AccessDeniedException;
import com.xuancong.employee_management.exception.DuplicateResourceException;
import com.xuancong.employee_management.exception.NotFoundException;
import com.xuancong.employee_management.model.Attendance;
import com.xuancong.employee_management.model.Employee;
import com.xuancong.employee_management.repository.AttendanceRepository;
import com.xuancong.employee_management.repository.EmployeeRepository;
import com.xuancong.employee_management.repository.UserRepository;
import com.xuancong.employee_management.utils.AuthenticationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;


    public AttendanceGetResponse checkIn(){
        Employee employee = this.getEmployeeCurrent();
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
        if(this.checkExitedAttendance(employee,workDate)){
            throw new DuplicateResourceException(Constants.ErrorKey.CHECKIN_DUPLICATE,workDate);
        }
    }

    private boolean checkExitedAttendance(Employee employee , LocalDate workDate){
        return attendanceRepository.existsByEmployeeAndWorkDate(employee, workDate);

    }


    private Employee getEmployeeCurrent(){
        Long userId = AuthenticationUtils.extractUserId();
        return  employeeRepository.findByUser_Id(userId)
                .orElseThrow(() -> new NotFoundException(Constants.ErrorKey.EMPLOYEE_NOT_FOUND,userId));

    }


    private Attendance getAttendanceOrThrow(Employee employee , LocalDate workDate){
        return attendanceRepository.findByEmployeeAndWorkDate(employee, workDate)
                .orElseThrow(()-> new NotFoundException(Constants.ErrorKey.CHECKIN_NOT_FOUND,workDate));
    }

    public void checkOut(){
        Employee employee = this.getEmployeeCurrent();
        LocalDate workDate = LocalDate.now();
        LocalDateTime timeCheckOut = LocalDateTime.now();

        Attendance attendance = this.getAttendanceOrThrow(employee, workDate);
        if(attendance.getCheckOut()!=null){
            throw new DuplicateResourceException(Constants.ErrorKey.CHECKOUT_DUPLICATE, workDate);
        }
        attendance.setCheckOut(timeCheckOut);
        attendanceRepository.save(attendance);

    }



    // read-state => gọi nhiều
    // fix sau : key được gọi lại mỗi lâần chạy fuction => query db nhiều lần => tốn performain => fix bằng userID có sẵn trong security context
    @Cacheable(
            value = "attendance:today",
            key = "#root.target.getEmployeeCurrent().getId() + ':' + T(java.time.LocalDate).now()"
    )
    public AttendanceStatusResponse getTodayAttendance(){
        Employee employee = this.getEmployeeCurrent();
        LocalDate workDate = LocalDate.now();
        Attendance attendance =  attendanceRepository.findByEmployeeAndWorkDate(
                employee,workDate
        ).orElse(null);
        return  AttendanceStatusResponse.from(attendance,employee.getId(),workDate);

    }





    @PreAuthorize("hasRole('ADMIN') or #employeeId == null")
    public AttendancePagingGetResponse getAttendances(LocalDate from, LocalDate to, int page,int size,Long employeeId){
        Pageable pageable = PageRequest.of(page,size);
        Page<Attendance> attendancePage;
        if(employeeId == null){
           employeeId = getEmployeeCurrent().getId();
        }else {
            if(!AuthenticationUtils.hasRole("ADMIN") && !employeeId.equals(getEmployeeCurrent().getId())){
                throw new AccessDeniedException(Constants.ErrorKey.FORBIDDEN);
            }
        }
        attendancePage =
                attendanceRepository.findByEmployeeIdAndWorkDateBetween(
                        employeeId, from, to, pageable
                );

        List<AttendanceGetResponse> content = attendancePage.getContent().stream()
                .map(AttendanceGetResponse::fromAttendance)
                .toList();
        return  new AttendancePagingGetResponse(
                content,
                (int) attendancePage.getTotalElements(),
                attendancePage.getTotalPages(),
                attendancePage.isLast()
        );


    }
}
