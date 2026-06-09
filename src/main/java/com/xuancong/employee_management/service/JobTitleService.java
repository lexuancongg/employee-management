package com.xuancong.employee_management.service;

import com.xuancong.employee_management.repository.JobTitleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class JobTitleService {
    private final JobTitleRepository jobTitleRepository;


}
