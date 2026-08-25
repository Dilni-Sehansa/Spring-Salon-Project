package com.example.Spring_Salon_Project.entity;

import com.example.Spring_Salon_Project.enumiration.CategoryStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @Column(nullable = false, unique = true)
    private String categoryName;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoryStatus categoryStatus;

}
