package com.xuancong.employee_management.exception;

import com.xuancong.employee_management.utils.MessageUtils;

import java.text.MessageFormat;

public class DuplicateResourceException extends RuntimeException{
    private final String message;
    public DuplicateResourceException(String message ,Object... args) {
        this.message = MessageUtils.getMessage(message, args);
    }

    @Override
    public String getMessage() {
        return message;
    }

}
