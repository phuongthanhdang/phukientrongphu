package com.example.demo.dto;

import com.example.demo.entity.Category;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class CategoryDto {
    private Long id;
    private String name;
    private String description;


}
