package com.xuancong.employee_management.dto.image;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.MediaType;

import java.io.InputStream;

@Builder
@Getter
public class ImageStreamResponse {
    private InputStream content;
    private MediaType mediaType;
}
