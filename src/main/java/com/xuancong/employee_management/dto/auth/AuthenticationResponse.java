package com.xuancong.employee_management.dto.auth;

import org.springframework.http.ResponseCookie;

public record AuthenticationResponse(
        String accessToken,
        String refreshToken,
        long expiresIn,
        String role
) {
}
