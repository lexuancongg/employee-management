package com.xuancong.employee_management.exception;

import com.xuancong.employee_management.utils.MessageUtils;

public class ResourceInUseException extends RuntimeException{
    private final String message;
    public ResourceInUseException(String message, Object ...params) {
        this.message = MessageUtils.getMessage(message, params);
    }

    @Override
    public String getMessage() {
        return message;
    }
}
