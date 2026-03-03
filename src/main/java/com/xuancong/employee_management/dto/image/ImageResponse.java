package com.xuancong.employee_management.dto.image;

import com.xuancong.employee_management.model.Image;

public record ImageResponse(
        Long id,
        String fileName,
        String filePath,
        String imageType
) {
    public static ImageResponse fromImage(Image image) {
        return new ImageResponse(
                image.getId(),
                image.getFileName(),
                image.getFilePath(),
                image.getImageType()
        );
    }
}
