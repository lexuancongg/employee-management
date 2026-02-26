package com.xuancong.employee_management.utils;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthenticationUtils {

    public static String extractUsername() {
        Authentication authentication = getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            // bắn ra ngoại leej
        }
        assert authentication != null;
        return authentication.getName();
    }

    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }



    public static boolean hasRole(String role) {
        Authentication authentication = getAuthentication();
        return authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(role));

    }
}