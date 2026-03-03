package com.xuancong.employee_management.controller;

import com.xuancong.employee_management.dto.image.ImageCreateRequest;
import com.xuancong.employee_management.dto.image.ImageResponse;
import com.xuancong.employee_management.service.ImageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/images")
public class ImageController {
    private final ImageService imageService;

    @PostMapping( consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ImageResponse> uploadImage(@ModelAttribute @Valid ImageCreateRequest imageCreateRequest){
        return ResponseEntity.ok(imageService.create(imageCreateRequest));
    }
}
