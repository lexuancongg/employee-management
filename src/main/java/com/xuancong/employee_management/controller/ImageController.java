package com.xuancong.employee_management.controller;

import com.xuancong.employee_management.dto.image.ImageCreateRequest;
import com.xuancong.employee_management.dto.image.ImageResponse;
import com.xuancong.employee_management.dto.image.ImageStreamResponse;
import com.xuancong.employee_management.service.ImageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/images")
public class ImageController {
    private final ImageService imageService;

    @PostMapping( consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ImageResponse> uploadImage(@ModelAttribute @Valid ImageCreateRequest imageCreateRequest){
        return ResponseEntity.ok(imageService.create(imageCreateRequest));
    }


    @GetMapping("/{id}/file/{fileName}")
    public ResponseEntity<InputStreamResource> getFile(@PathVariable Long id, @PathVariable String fileName) {
        ImageStreamResponse imageStreamResponse = this.imageService.getFile(id,fileName);
        return ResponseEntity.ok()
                .contentType(imageStreamResponse.getMediaType())
                .body(new InputStreamResource(imageStreamResponse.getContent()));
    }
}
