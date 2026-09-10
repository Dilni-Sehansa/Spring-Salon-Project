package com.example.Spring_Salon_Project.dto;

import com.example.Spring_Salon_Project.enumiration.CategoryStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CategoryDTO {
    private Long categoryId;

    @NotBlank(message = "Category name is required")
    @Size(min = 2, max = 100)
    private String categoryName;
    private String description;
    private CategoryStatus categoryStatus;
}
