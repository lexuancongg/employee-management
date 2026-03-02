package com.xuancong.employee_management.dto.auth;

public record AuthStateResponse(
        boolean isAuthenticated,
        AuthenticatedUserResponse authenticatedUser

){
}
