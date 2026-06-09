package com.xuancong.employee_management.controller;

import com.xuancong.employee_management.dto.leaverequest.LeaveRequestCreateRequest;
import com.xuancong.employee_management.model.LeaveRequest;
import com.xuancong.employee_management.service.LeaveRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class LeaveRequestController {
    private final LeaveRequestService leaveRequestService;
    @PostMapping("/leave-request")
    public ResponseEntity<LeaveRequest> create(@Valid @RequestBody LeaveRequestCreateRequest leaveRequestCreateRequest){
        return ResponseEntity.ok(
                this.leaveRequestService.create(leaveRequestCreateRequest)
        );
    }
}
