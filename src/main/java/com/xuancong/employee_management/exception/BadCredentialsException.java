package com.xuancong.employee_management.exception;

import com.xuancong.employee_management.utils.MessageUtils;

public class BadCredentialsException extends RuntimeException{
    private final String message;
    public BadCredentialsException(String message , Object... args) {
        this.message = MessageUtils.getMessage(message, args);
    }

    @Override
    public String getMessage() {
        return message;
    }
}
