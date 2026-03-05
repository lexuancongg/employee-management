package com.xuancong.employee_management.dto.auth;

public record LoginResponse (
        String role,
        long expiresIn
){
}
