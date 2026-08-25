package com.example.Spring_Salon_Project.service;

import com.example.Spring_Salon_Project.dto.CategoryDTO;
import java.util.List;

public interface CategoryService {
    CategoryDTO saveCategory(CategoryDTO categoryDTO);

    void updateCategory(CategoryDTO categoryDTO);

    CategoryDTO getCategoryDetails(String categoryName);

    List<CategoryDTO> getAllCategories();

    List<CategoryDTO> filterCategory(String categoryName);

    CategoryDTO selectCategory(long categoryId);

    void deleteCategory(long categoryId);


}
