package com.xuancong.employee_management.service;

import com.xuancong.employee_management.config.FilesystemConfig;
import com.xuancong.employee_management.dto.image.ImageCreateRequest;
import com.xuancong.employee_management.dto.image.ImageResponse;
import com.xuancong.employee_management.dto.image.ImageStreamResponse;
import com.xuancong.employee_management.model.Image;
import com.xuancong.employee_management.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
@Transactional
public class ImageService {
    private final ImageRepository imageRepository;
    private final FilesystemConfig filesystemConfig;
    private final S3FileService s3FileService;


    public ImageResponse create(ImageCreateRequest imageCreateRequest) {
        Image image = imageCreateRequest.toImage();
        try {
            // dữ liệu của file được  the hien duoi dang file
            String filePath = this.saveFileInFilesystem(image.getFileName() , imageCreateRequest.file().getBytes());
            String filePathS3 =  s3FileService.uploadFile(image.getFileName(),imageCreateRequest.file().getBytes());
            System.out.println(filePathS3);
            image.setFilePath(filePath);
            imageRepository.save(image);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ImageResponse.fromImage(image);

    }

    private String saveFileInFilesystem(String fileName, byte[] contentFile){
        File directory = new File(filesystemConfig.getDirectory());
        this.checkIsExitedDirectory(directory);
        this.validateFileName(fileName);
        Path filePath = this.buildFilePath(fileName);
        try {
            Files.write(filePath, contentFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return filePath.toString();
    }

    private Path buildFilePath(String fileName) {
        Path path = Paths.get(filesystemConfig.getDirectory(), fileName).toAbsolutePath().normalize();
        return path;
    }

    private void validateFileName(String fileName) {
        if (fileName.contains("..") || fileName.contains("/") || fileName.contains("\\")) {
            // bắn ra ngoại lệ
        }
    }

    private void checkIsExitedDirectory(File directory) {
        if(!directory.exists()) {
            directory.mkdirs();
        }
    }

    public ImageStreamResponse getFile(Long id, String fileName) {


        Image image = imageRepository.findById(id).orElse(null);
        if (image == null || !fileName.equalsIgnoreCase(image.getFileName())) {
            return null;
        }
        MediaType mediaType = MediaType.valueOf(image.getImageType());
        InputStream streamFile = this.getStreamFile(image.getFilePath());

        return ImageStreamResponse.builder()
                .mediaType(mediaType)
                .content(streamFile)
                .build();

    }
    public InputStream getStreamFile(String filePath){
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            throw new IllegalStateException("not found");
        }

        try {
            return Files.newInputStream(path);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file: " + filePath, e);
        }
    }

}
