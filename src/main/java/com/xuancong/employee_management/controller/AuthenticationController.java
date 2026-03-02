package com.xuancong.employee_management.controller;

import com.xuancong.employee_management.dto.auth.*;
import com.xuancong.employee_management.model.User;
import com.xuancong.employee_management.repository.UserRepository;
import com.xuancong.employee_management.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthenticationController {
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;


    @GetMapping("/authentication")
    public ResponseEntity<AuthStateResponse> getAuthenticationInfo(@AuthenticationPrincipal Long userId) {
        if(userId == null) {
            return ResponseEntity.ok(new AuthStateResponse(false, null));
        }
        Optional<User> user = userRepository.findById(userId);
        return user.map(value -> ResponseEntity.ok(new AuthStateResponse(
                true,
                new AuthenticatedUserResponse(value.getUsername())
        ))).orElseGet(() -> ResponseEntity.ok(new AuthStateResponse(false, null)));

    }


    @GetMapping("/authentication/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody AuthRequest authRequest
            ){
        return ResponseEntity.ok(
                authenticationService.login(authRequest)
        );
    }

    @PostMapping("/authentication/refresh")
    public ResponseEntity<AuthenticationResponse> refresh(
            @RequestBody RefreshRequest refreshRequest
    ){
        return ResponseEntity.ok(
                this.authenticationService.refresh(refreshRequest)
        );
    }




}
