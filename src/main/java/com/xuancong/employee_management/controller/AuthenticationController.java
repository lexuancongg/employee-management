package com.xuancong.employee_management.controller;

import com.xuancong.employee_management.dto.auth.*;
import com.xuancong.employee_management.model.User;
import com.xuancong.employee_management.repository.UserRepository;
import com.xuancong.employee_management.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
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


    @PostMapping("/authentication/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody AuthRequest authRequest
    ) {

        AuthenticationResponse authResponse =
                authenticationService.login(authRequest);

        ResponseCookie accessCookie = ResponseCookie.from("accessToken", authResponse.accessToken())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(authResponse.expiresIn())
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", authResponse.refreshToken())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(7 * 24 * 3600)
                .build();

        return ResponseEntity.ok()
                .headers(headers -> {
                    headers.add(HttpHeaders.SET_COOKIE, accessCookie.toString());
                    headers.add(HttpHeaders.SET_COOKIE, refreshCookie.toString());
                })
                .body(new LoginResponse(
                        authResponse.role(),
                        authResponse.expiresIn()
                ));
    }

    @PostMapping("/authentication/refresh")
    public ResponseEntity<AuthenticationResponse> refresh(
            @RequestBody RefreshRequest refreshRequest
    ){
        return ResponseEntity.ok(
                this.authenticationService.refresh(refreshRequest)
        );
    }



    @PostMapping("/authentication/logout")
    public ResponseEntity<Void> logout(
            @RequestBody @Valid LogoutRequest logoutRequest
    ){
        authenticationService.logout(logoutRequest);
        return ResponseEntity.noContent().build();
    }


}
