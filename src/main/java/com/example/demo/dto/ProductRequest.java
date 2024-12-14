package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ProductRequest {
    private String productName;
    private double productPrice;
    private MultipartFile productImage;
    private Long categoryId;
}
