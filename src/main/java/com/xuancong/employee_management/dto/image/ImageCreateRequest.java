package com.xuancong.employee_management.dto.image;

import com.xuancong.employee_management.model.Image;
import com.xuancong.employee_management.validation.ValidateTypeFile;
import org.springframework.web.multipart.MultipartFile;

public record ImageCreateRequest(
        @ValidateTypeFile(allowedTypes = {"image/jpeg", "image/png", "image/gif"}) MultipartFile file

) {
    public Image toImage(){
        Image image = new Image();
        image.setFileName(file.getOriginalFilename());
        image.setImageType(file.getContentType());
        return image;

    }
}
