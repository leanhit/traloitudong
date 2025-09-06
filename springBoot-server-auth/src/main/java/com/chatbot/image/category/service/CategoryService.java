package com.chatbot.image.category.service;

import com.chatbot.image.category.dto.CategoryRequestDTO;
import com.chatbot.image.category.dto.CategoryResponseDTO;
import com.chatbot.image.category.model.Category; // Correct import for Category model
import com.chatbot.image.category.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    // --- Phương thức chuyển đổi Entity -> DTO ---
    private CategoryResponseDTO convertToResponseDTO(Category category) {
        return new CategoryResponseDTO(
            category.getId(),
            category.getName(),
            category.getDescription(),
            category.getCreatedDate()
        );
    }
    
    // --- CREATE (Tạo mới) ---
    @Transactional
    public CategoryResponseDTO createCategory(CategoryRequestDTO requestDTO) {
        if (categoryRepository.findByName(requestDTO.getName()).isPresent()) {
            throw new IllegalArgumentException("Danh mục đã tồn tại với tên: " + requestDTO.getName());
        }
        Category newCategory = new Category();
        newCategory.setName(requestDTO.getName());
        newCategory.setDescription(requestDTO.getDescription());
        Category savedCategory = categoryRepository.save(newCategory);
        return convertToResponseDTO(savedCategory);
    }

    // --- READ (Đọc tất cả) ---
    public List<CategoryResponseDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
            .map(this::convertToResponseDTO)
            .collect(Collectors.toList());
    }

    // --- READ (Đọc theo ID) - Trả về DTO cho client
    public Optional<CategoryResponseDTO> getCategoryDtoById(String id) {
        return categoryRepository.findById(id).map(this::convertToResponseDTO);
    }

    // --- READ (Đọc theo ID) - Trả về Entity cho các Service nội bộ
    public Optional<Category> getCategoryById(String id) {
        return categoryRepository.findById(id);
    }
    
    // --- UPDATE (Cập nhật) ---
    @Transactional
    public CategoryResponseDTO updateCategory(String id, CategoryRequestDTO requestDTO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục với id: " + id));
        
        category.setName(requestDTO.getName());
        category.setDescription(requestDTO.getDescription());
        category.setUpdatedDate(LocalDateTime.now());
        
        Category updatedCategory = categoryRepository.save(category);
        return convertToResponseDTO(updatedCategory);
    }

    // --- DELETE (Xóa) ---
    @Transactional
    public void deleteCategory(String id) {
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy danh mục với id: " + id);
        }
        categoryRepository.deleteById(id);
    }
}