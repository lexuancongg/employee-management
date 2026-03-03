package com.xuancong.employee_management.exception;

import com.xuancong.employee_management.utils.MessageUtils;

public class UnauthorizedException extends RuntimeException{
    private final String message;
    public UnauthorizedException(String message, Object ...params) {
        this.message = MessageUtils.getMessage(message, params);
    }

    @Override
    public String getMessage() {
        return message;
    }
}
