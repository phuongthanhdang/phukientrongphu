package com.example.demo.service.impl;

import com.example.demo.dto.ProductDto;
import com.example.demo.dto.ProductRequest;
import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import com.example.demo.respository.CategoryRepository;
import com.example.demo.respository.ProductRepository;
import com.example.demo.service.ProductService;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final String UPLOAD_DIR = "uploads/";
    @Override
    public List<ProductDto> getProductByCategoryId(Long categoryId) {
        List<Product> products = productRepository.findByCategoryId(categoryId);
        return convertToDto(products);
    }

    @Override
    public ResponseEntity<String> createProduct(ProductRequest productRequest) {
        Optional<Category> optionalCategory = categoryRepository.findById(productRequest.getCategoryId());
        if (optionalCategory.isEmpty()) {
            throw new RuntimeException("Category not found with id: " + productRequest.getCategoryId());
        }
        // Lưu file ảnh
        String imagePath = saveImage(productRequest.getProductImage());


        Product product = new Product();
        product.setName(productRequest.getProductName());
        product.setPrice(productRequest.getProductPrice());
        product.setImageUrl(imagePath);
        product.setCategory(optionalCategory.get());
        try {
            productRepository.save(product);
            return ResponseEntity.ok().body("Success");
        } catch (Exception ex){
            throw new RuntimeException("Error save product");
        }
    }

    @Override
    public List<ProductDto> getProduct() {
        List<Product> product = productRepository.findAll();
        return convertToDto(product);
    }

    @Override
    public ResponseEntity<String> deleteProduct(Long productId) {
        try {
            Optional<Product> product = productRepository.findById(productId);
            if (product.isEmpty()){
                throw new BadRequestException("Sản phẩm không tồn tại");
            }
            // Lấy đường dẫn ảnh từ đối tượng Category
            String imagePath = product.get().getImageUrl(); // Giả sử đường dẫn lưu trong 'imageUrl'

            // Tạo đối tượng File từ đường dẫn ảnh
            File imageFile = new File("src/main/resources/static/" + imagePath); // Đảm bảo đường dẫn là chính xác

            // Kiểm tra xem tệp hình ảnh có tồn tại không
            if (imageFile.exists()) {
                try {
                    Files.delete(Paths.get(imageFile.getAbsolutePath())); // Xóa tệp hình ảnh
                    System.out.println("Image deleted successfully: " + imageFile.getAbsolutePath());
                } catch (IOException e) {
                    return ResponseEntity.status(500).body("Failed to delete image: " + e.getMessage());
                }
            }
            productRepository.delete(product.get());
            return ResponseEntity.ok().body("Success");
        }catch (Exception e){
            throw new RuntimeException("Delete product error {}", e);
        }
    }

    private String saveImage(MultipartFile file) {
        try {
            // Định nghĩa đường dẫn thư mục static
            String uploadDir = new File("src/main/resources/static/upload/").getAbsolutePath();
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs(); // Tạo thư mục nếu chưa tồn tại
            }

            // Đường dẫn lưu ảnh trong thư mục static/upload
            String fileName = file.getOriginalFilename();
            Path imagePath = Paths.get(uploadDir, fileName);

            // Lưu tệp
            Files.copy(file.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);

            // Trả về đường dẫn tương đối (để sử dụng trong HTML hoặc API)
            return "upload/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file: " + e.getMessage());
        }
    }

    private List<ProductDto> convertToDto(List<Product> products){
        List<ProductDto> productDtos = new ArrayList<>();
        for (Product product : products) {
            ProductDto productDto = new ProductDto();
            productDto.setId(product.getId());
            productDto.setProductName(product.getName());
            productDto.setProductPrice(product.getPrice());
            productDto.setProductImage(product.getImageUrl());
            productDtos.add(productDto);
        }
        return productDtos;
    }
}
