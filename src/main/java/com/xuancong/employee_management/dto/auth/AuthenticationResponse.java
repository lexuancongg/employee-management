package com.xuancong.employee_management.dto.auth;

public record AuthenticationResponse(
        String accessToken,
        String refreshToken,
        long expiresIn
) {
}
