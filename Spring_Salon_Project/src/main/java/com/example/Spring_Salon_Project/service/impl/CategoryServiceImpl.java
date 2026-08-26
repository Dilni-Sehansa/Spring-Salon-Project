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

//           if(categoryRepository.existsByCategoryName(categoryDTO.getCategoryName())){
//               throw new CustomerException(400, "Category name already exists!");
//           }

           Optional<Category> existingCategory = categoryRepository.findByCategoryName(categoryDTO.getCategoryName());

           if (existingCategory.isPresent()) {
               Category category = existingCategory.get();

               if (category.getCategoryStatus() == CategoryStatus.INACTIVE) {
                   throw new CustomerException(400, "This category already exists (previously saved/deleted). Please restore or activate it instead of creating a new one!");
               } else {
                   throw new CustomerException(400, "Category name already exists and is currently active!");
               }
           }
           Category category = new Category();
           category.setCategoryName(categoryDTO.getCategoryName());
           category.setDescription(categoryDTO.getDescription());
//           category.setCategoryStatus(categoryDTO.getCategoryStatus());
           category.setCategoryStatus(categoryDTO.getCategoryStatus() != null ? categoryDTO.getCategoryStatus() : CategoryStatus.ACTIVE);

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
//            category.setCategoryName(categoryDTO.getCategoryName());
//            category.setDescription(categoryDTO.getDescription());

            if (categoryDTO.getCategoryName() != null && !categoryDTO.getCategoryName().equalsIgnoreCase(category.getCategoryName())) {
                Optional<Category> existingCategory = categoryRepository.findByCategoryName(categoryDTO.getCategoryName());
                if (existingCategory.isPresent()) {
                    throw new CustomerException(400, "Category name already exists!");
                }
                category.setCategoryName(categoryDTO.getCategoryName());
            }

            if (categoryDTO.getDescription() != null) {
                category.setDescription(categoryDTO.getDescription());
            }

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

    @Override
    public void changeCategoryStatus(long categoryId) {
        log.info("Execute method changeCategoryStatus for categoryId: {}", categoryId);

        try {
            Optional<Category> optionalCategory = categoryRepository.findById(categoryId);

            if (optionalCategory.isPresent()) {
                Category category = optionalCategory.get();

                if (category.getCategoryStatus() == CategoryStatus.ACTIVE) {
                    category.setCategoryStatus(CategoryStatus.INACTIVE);
                } else {
                    category.setCategoryStatus(CategoryStatus.ACTIVE);
                }

                categoryRepository.save(category);
                log.info("Category status updated successfully");
            } else {
                throw new CustomerException(404, "Category not found");
            }
        } catch (Exception e) {
            log.error("Error in method changeCategoryStatus: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public List<CategoryDTO> filterCategory(String categoryName, CategoryStatus categoryStatus) {
        return categoryRepository.filterCategory(categoryName, categoryStatus);
    }


}
