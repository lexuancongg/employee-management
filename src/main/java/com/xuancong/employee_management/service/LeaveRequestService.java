package com.xuancong.employee_management.service;

import com.xuancong.employee_management.dto.leaverequest.LeaveRequestCreateRequest;
import com.xuancong.employee_management.dto.ws.NotificationMessage;
import com.xuancong.employee_management.model.Employee;
import com.xuancong.employee_management.model.LeaveRequest;
import com.xuancong.employee_management.model.LeaveStatus;
import com.xuancong.employee_management.model.NotificationType;
import com.xuancong.employee_management.repository.EmployeeRepository;
import com.xuancong.employee_management.repository.LeaveRequestRepository;
import com.xuancong.employee_management.utils.AuthenticationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LeaveRequestService {
    private final LeaveRequestRepository leaveRequestRepository;
    private final EmployeeRepository employeeRepository;
    private final NotificationService notificationService;


    public LeaveRequest create(LeaveRequestCreateRequest dto) {
        Long userId = AuthenticationUtils.extractUserId();


        Employee employee = employeeRepository.findByUser_Id(userId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        if (dto.getStartDate().isAfter(dto.getEndDate())) {
            throw new RuntimeException("Start date must be before end date");
        }

        LeaveRequest request = LeaveRequest.builder()
                .employee(employee)
                .leaveType(dto.getLeaveType())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .reason(dto.getReason())
                .status(LeaveStatus.PENDING)
                .build();

        LeaveRequest saved = leaveRequestRepository.save(request);

        Employee manager = employee.getDepartment().getManager();

        notificationService.createNotification(
                manager,
                "Đơn xin nghỉ phép mới",
                employee.getName() + " vừa gửi đơn nghỉ phép",
                NotificationType.LEAVE,
                saved.getId()
        );

        notificationService.sendToUser(
                manager.getId(),
                new NotificationMessage(
                        manager.getId(),
                        "Đơn xin nghỉ phép mới",
                        employee.getName() + " vừa gửi đơn",
                        "LEAVE"
                )
        );

        return saved;
    }
}
