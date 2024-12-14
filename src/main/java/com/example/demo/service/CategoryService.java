package com.example.demo.service;

import com.example.demo.dto.CategoryDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CategoryService {
    List<CategoryDto> getAllCategory();
    ResponseEntity<String> createCategory(String name, MultipartFile file) throws IOException;
    ResponseEntity<String> deleteProduct (Long categoryId);

}
