package com.xuancong.employee_management.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {
    List<T> content;
    int totalElements;
    int totalPages;
    boolean isLast;
}