package com.xuancong.employee_management.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class FilesystemConfig {
    @Value("${filesystem.directory}")
    private String directory;
}
