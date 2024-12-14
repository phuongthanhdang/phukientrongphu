package com.example.demo.controller;

import com.example.demo.dto.CategoryDto;
import com.example.demo.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public List<CategoryDto> getCategories() {
        return categoryService.getAllCategory();
    }
    @PostMapping
    public ResponseEntity<String> createCategory(@RequestParam("name") String name,
                                                 @RequestParam("image") MultipartFile image)  throws IOException {
        return categoryService.createCategory(name, image);
    }
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long categoryId) {
        try {
            return categoryService.deleteProduct(categoryId);
        } catch (Exception e) {
            throw new RuntimeException("Get product error");
        }
    }
}
