package com.example.Spring_Salon_Project.controller;

import com.example.Spring_Salon_Project.dto.CategoryDTO;
import com.example.Spring_Salon_Project.dto.CommonResponse;
import com.example.Spring_Salon_Project.enumiration.AppointmentStatus;
import com.example.Spring_Salon_Project.enumiration.CategoryStatus;
import com.example.Spring_Salon_Project.security.JwtUtil;
import com.example.Spring_Salon_Project.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "v1/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final JwtUtil jwtUtil;

    @PostMapping(value = "/category-saved" , produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse saveCategory(@Valid @RequestBody CategoryDTO categoryDTO){
        CategoryDTO saveCategory = categoryService.saveCategory(categoryDTO);
        return new CommonResponse(0, saveCategory, "Category saved successfully");
    }

    @GetMapping(value = "/category", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAllCategory(){
        List<CategoryDTO> getCategory = categoryService.getAllCategories();
        return new CommonResponse(0,getCategory,"Category Loaded Successfully");
    }

    @DeleteMapping(value = "/{categoryId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse deleteCategory(@PathVariable long categoryId){
        categoryService.deleteCategory(categoryId);
        return new CommonResponse(0,"Category Deleted Successfully");
    }

    @GetMapping(value = "/select-category/{categoryId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse selectCategory(@PathVariable long categoryId){
        CategoryDTO selectCategory = categoryService.selectCategory(categoryId);
        return new CommonResponse(0,selectCategory,"Category Loaded Successfully");
    }

    @PutMapping(value = "/update-category",produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updateCategory(@Valid @RequestBody CategoryDTO categoryDTO){
        categoryService.updateCategory(categoryDTO);
        return new CommonResponse(0,"Category Updated Successfully");
    }

    @GetMapping(value = "/details/{categoryName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getCategoryDetails(@PathVariable String categoryName) {
        CategoryDTO categoryDTO = categoryService.getCategoryDetails(categoryName);
        return new CommonResponse(0, categoryDTO, "Category Loaded Successfully");
    }

    @PatchMapping(value = "/update-status/{categoryId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse changeCategoryStatus(
            @PathVariable long categoryId,
            @RequestParam CategoryStatus status) {
        categoryService.changeCategoryStatus(categoryId, status);
        return new CommonResponse(0, "Category Status Changed Successfully");
    }

    @GetMapping("/search")
    public CommonResponse filterCategory(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String status) {

        CategoryStatus categoryStatus = null;

        if (status != null && !status.trim().isEmpty() && !status.equalsIgnoreCase("all")) {
            try {
                categoryStatus = CategoryStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                categoryStatus = null;
            }
        }

        List<CategoryDTO> list = categoryService.filterCategory(name, categoryStatus);
        return new CommonResponse(0, list, "Category Filtered Successfully");
    }
}
