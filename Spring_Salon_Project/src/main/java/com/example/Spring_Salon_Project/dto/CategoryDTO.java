package com.example.Spring_Salon_Project.dto;

import com.example.Spring_Salon_Project.enumiration.CategoryStatus;
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
    private String categoryName;
    private String description;
    private CategoryStatus categoryStatus;
}
