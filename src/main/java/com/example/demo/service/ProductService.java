package com.example.demo.service;

import com.example.demo.dto.ProductDto;
import com.example.demo.dto.ProductRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {
    List<ProductDto> getProductByCategoryId(Long categoryId);
    ResponseEntity<String> createProduct(ProductRequest productRequest);
    List<ProductDto> getProduct();
    ResponseEntity<String> deleteProduct(Long productId);

}
