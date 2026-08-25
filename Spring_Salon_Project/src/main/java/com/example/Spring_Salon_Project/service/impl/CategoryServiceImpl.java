package com.example.Spring_Salon_Project.service.impl;

import com.example.Spring_Salon_Project.dto.CategoryDTO;
import com.example.Spring_Salon_Project.entity.Category;
import com.example.Spring_Salon_Project.enumiration.CategoryStatus;
import com.example.Spring_Salon_Project.exception.CustomerException;
import com.example.Spring_Salon_Project.repository.CategoryRepository;
import com.example.Spring_Salon_Project.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    @Override
    public CategoryDTO saveCategory(CategoryDTO categoryDTO) {
       log.info("Execute method saveCategory");

       try{
           Category category = new Category();
           category.setCategoryName(categoryDTO.getCategoryName());
           category.setDescription(categoryDTO.getDescription());
           category.setCategoryStatus(categoryDTO.getCategoryStatus());

           Category save = categoryRepository.save(category);
           log.info("Category saved successfully");

           return new CategoryDTO(save.getCategoryId(), save.getCategoryName(), save.getDescription(), save.getCategoryStatus());
       } catch (Exception e) {
           log.error("Error in method saving Category: {}",e.getMessage());
           throw e;

       }
    }

    @Override
    public void updateCategory(CategoryDTO categoryDTO) {
        log.info("Execute method updateCategory");

        try {
            Optional<Category> updateUser = categoryRepository.findById(categoryDTO.getCategoryId());

            if(updateUser.isEmpty())
                throw new CustomerException(404, "Sorry, category not found");

            Category category = updateUser.get();
            category.setCategoryName(categoryDTO.getCategoryName());
            category.setDescription(categoryDTO.getDescription());

            if (categoryDTO.getCategoryStatus() != null) {
                category.setCategoryStatus(categoryDTO.getCategoryStatus());
            }
            categoryRepository.save(category);

        }catch (Exception e){
            log.error("Error in method updateCategory: {}",e.getMessage());
            throw e;
        }

    }

    @Override
    public CategoryDTO getCategoryDetails(String categoryName) {
        try {
            Optional<Category> optionalCategory = categoryRepository.findByCategoryName(categoryName);

            if(optionalCategory.isEmpty())
                throw new CustomerException(404, "Sorry, category not found");

            Category category = optionalCategory.get();

            if (category.getCategoryStatus() == CategoryStatus.INACTIVE) {
                throw new CustomerException(404, "Sorry, category not found");
            }

            return new CategoryDTO(category.getCategoryId(), category.getCategoryName(), category.getDescription(), category.getCategoryStatus());
        }catch (Exception e){
            log.error("Error in method getCategoryDetails: {}",e.getMessage());
            throw e;
        }

    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.getAllCategory();
    }

    @Override
    public List<CategoryDTO> filterCategory(String categoryName) {
        return categoryRepository.filterCategory(categoryName);
    }

    @Override
    public CategoryDTO selectCategory(long categoryId) {
        CategoryDTO categoryDTO = categoryRepository.selectCategory(categoryId);
        if (categoryDTO == null) {
            throw new CustomerException(404, "Category not found for ID: " + categoryId);
        }
        return categoryDTO;
    }

    @Override
    public void deleteCategory(long categoryId) {
        log.info("Execute method deleteCategory() categoryId{}",categoryId);

        try {
            Optional<Category> optionalCategory = categoryRepository.findById(categoryId);

            if (optionalCategory.isEmpty() || optionalCategory.get().getCategoryStatus() == CategoryStatus.INACTIVE) {
                throw new CustomerException(404, "Category not found");
            }

            Category category = optionalCategory.get();
            category.setCategoryStatus(CategoryStatus.INACTIVE);
            categoryRepository.save(category);
        }catch (Exception e){
            log.error("Error deleting category");
            throw e;
        }
    }
}
