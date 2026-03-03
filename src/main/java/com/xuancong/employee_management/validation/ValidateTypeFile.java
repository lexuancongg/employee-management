package com.xuancong.employee_management.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        // class chứa logic để kiểm tra phần validate
        validatedBy = {TypeFileValidator.class}
)
public @interface ValidateTypeFile {
    String message() default "invalid type file";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
    String[] allowedTypes() ;
}
