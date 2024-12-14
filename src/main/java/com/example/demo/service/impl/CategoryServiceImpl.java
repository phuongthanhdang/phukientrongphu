package com.example.demo.service.impl;

import com.example.demo.dto.CategoryDto;
import com.example.demo.dto.ProductDto;
import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import com.example.demo.respository.CategoryRepository;
import com.example.demo.respository.ProductRepository;
import com.example.demo.service.CategoryService;
import lombok.RequiredArgsConstructor;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    @Override
    public List<CategoryDto> getAllCategory() {
        List<Category> categories = categoryRepository.findCategory();
        return convertToDto(categories);
    }

    @Override
    public ResponseEntity<String> createCategory(String name, MultipartFile file) throws IOException {
        String imageUrl = saveImage(file);
        Category category = new Category(name, imageUrl);
        categoryRepository.save(category);
        return ResponseEntity.ok().body("Success");
    }

    @Override
    public ResponseEntity<String> deleteProduct(Long categoryId) {
        try {
            Optional<Category> category = categoryRepository.findById(categoryId);
            List<Product> products = productRepository.findByCategoryId(categoryId);
            if (category.isEmpty()){
                throw new BadRequestException("Danh mục không tồn tại");
            }
            List<String> imageProducts = new ArrayList<>();
            // Lấy đường dẫn ảnh từ đối tượng Category
            String imagePath = category.get().getImageUrl(); // Giả sử đường dẫn lưu trong 'imageUrl'
            // Nếu có sản phẩm, thêm đường dẫn ảnh của từng sản phẩm vào danh sách
            if (!products.isEmpty()) {
                imageProducts = products.stream()
                        .map(Product::getImageUrl)  // Lấy imageUrl của từng sản phẩm
                        .collect(Collectors.toList()); // Lưu vào danh sách
            }
            imageProducts.add(imagePath);

            for (String imageProduct : imageProducts) {
                // Tạo đối tượng File từ đường dẫn ảnh
                File imageFile = new File("src/main/resources/static/" + imageProduct);

                // Kiểm tra xem tệp hình ảnh có tồn tại không
                if (imageFile.exists()) {
                    try {
                        Files.delete(Paths.get(imageFile.getAbsolutePath())); // Xóa tệp hình ảnh
                        System.out.println("Image deleted successfully: " + imageFile.getAbsolutePath());
                    } catch (IOException e) {
                        return ResponseEntity.status(500).body("Failed to delete image: " + e.getMessage());
                    }
                }
            }
            categoryRepository.delete(category.get());
            return ResponseEntity.ok().body("Success");
        }catch (Exception e){
            throw new RuntimeException("Delete category error {}", e);
        }
    }

    private String saveImage(MultipartFile image) throws IOException {
        // Định nghĩa đường dẫn thư mục static
        String uploadDir = new File("src/main/resources/static/upload/").getAbsolutePath();
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs(); // Tạo thư mục nếu chưa tồn tại
        }

        // Đường dẫn lưu ảnh trong thư mục static/upload
        String fileName = image.getOriginalFilename();
        Path imagePath = Paths.get(uploadDir, fileName);

        // Lưu tệp
        Files.copy(image.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);

        // Trả về đường dẫn tương đối (để sử dụng trong HTML hoặc API)
        return "upload/" + fileName;
    }

    public List<CategoryDto> convertToDto(List<Category> categorys) {
        List<CategoryDto> categoryDtos = new ArrayList<>();
        for (Category category : categorys) {
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setId(category.getId());
            categoryDto.setName(category.getName());
            categoryDto.setImageUrl(category.getImageUrl());
            categoryDtos.add(categoryDto);
        }

        return categoryDtos;
    }
}
