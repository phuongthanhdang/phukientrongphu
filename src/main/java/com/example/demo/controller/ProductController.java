package com.example.demo.controller;

import com.example.demo.dto.ProductDto;
import com.example.demo.dto.ProductRequest;
import com.example.demo.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@AllArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/{categoryId}")
    public List<ProductDto> getProductByCategory(@PathVariable Long categoryId) {
        try {
            return productService.getProductByCategoryId(categoryId);
        } catch (Exception e) {
            throw new RuntimeException("Get product error");
        }
    }

    @PostMapping
    public ResponseEntity<String> createProduct(@ModelAttribute ProductRequest productRequest) {
        try {
            productService.createProduct(productRequest);
            return ResponseEntity.ok("Product created success");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public List<ProductDto> getProducts() {
        try {
            return productService.getProduct();
        } catch (Exception e) {
            throw new RuntimeException("Get product error");
        }
    }
    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId) {
        try {
            return productService.deleteProduct(productId);
        } catch (Exception e) {
            throw new RuntimeException("Get product error");
        }
    }

}
