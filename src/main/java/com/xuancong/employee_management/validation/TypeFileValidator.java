package com.xuancong.employee_management.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class TypeFileValidator implements ConstraintValidator<ValidateTypeFile, MultipartFile> {
    private String[] allowedTypes;
    private String message;
    @Override
    public void initialize(ValidateTypeFile constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        allowedTypes = constraintAnnotation.allowedTypes();
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(MultipartFile fileInput, ConstraintValidatorContext constraintValidatorContext) {
        if (fileInput == null || fileInput.getContentType() == null)  {
            // tắt messaga default
            constraintValidatorContext.disableDefaultConstraintViolation();
            // build lai message custom
            constraintValidatorContext.buildConstraintViolationWithTemplate(message).addConstraintViolation();
            return false;
        }
        for (String allowedType : allowedTypes) {
            if(fileInput.getContentType().equals(allowedType)) {
//                BufferedImage : đại diện hình ảnh được lưu trong ram
                try {
                    BufferedImage image = ImageIO.read(fileInput.getInputStream());
                    return image != null;
                } catch (IOException e) {
                    return false;
                }
            }
        }
        constraintValidatorContext.disableDefaultConstraintViolation();
        // build lai message custom
        constraintValidatorContext.buildConstraintViolationWithTemplate(message).addConstraintViolation();
        return false;

    }
}
