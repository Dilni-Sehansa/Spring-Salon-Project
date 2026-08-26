package com.example.Spring_Salon_Project.repository;


import com.example.Spring_Salon_Project.dto.CategoryDTO;
import com.example.Spring_Salon_Project.entity.Category;
import com.example.Spring_Salon_Project.enumiration.CategoryStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Long> {

    Optional<Category> findByCategoryName(String categoryName);

    @Query("""
      SELECT NEW com.example.Spring_Salon_Project.dto.CategoryDTO(
          c.categoryId,
          c.categoryName,
          c.description,
          c.categoryStatus
      )
      FROM Category c
            ORDER BY c.categoryId DESC
      """)
    List<CategoryDTO> getAllCategory();

    @Query("""
            SELECT new com.example.Spring_Salon_Project.dto.CategoryDTO(
                c.categoryId,
                c.categoryName,
                c.description,
                c.categoryStatus
            )
            FROM Category c
            WHERE :categoryName IS NULL
               OR :categoryName = ''
               OR LOWER(c.categoryName) LIKE LOWER(CONCAT('%', :categoryName, '%'))
               OR LOWER(c.description) LIKE LOWER(CONCAT('%', :categoryName, '%'))
            ORDER BY c.categoryId DESC
            """)
    List<CategoryDTO> filterCategory(@Param("categoryName") String categoryName);

    @Query("""
         SELECT new com.example.Spring_Salon_Project.dto.CategoryDTO(
              c.categoryId,
              c.categoryName,
              c.description,
              c.categoryStatus
         )
        FROM Category c
            WHERE c.categoryId = :categoryId
         """)
    CategoryDTO selectCategory(@Param("categoryId") Long categoryId);


    boolean existsByCategoryName(String categoryName);

    @Query("""
    SELECT new com.example.Spring_Salon_Project.dto.CategoryDTO(
        c.categoryId,
        c.categoryName,
        c.description,
        c.categoryStatus
    )
    FROM Category c
    WHERE (:categoryName IS NULL OR :categoryName = '' OR LOWER(c.categoryName) LIKE LOWER(CONCAT('%', :categoryName, '%')) OR LOWER(c.description) LIKE LOWER(CONCAT('%', :categoryName, '%')))
      AND (:status IS NULL OR c.categoryStatus = :status)
    ORDER BY c.categoryId DESC
    """)
    List<CategoryDTO> filterCategory(@Param("categoryName") String categoryName, @Param("status") CategoryStatus categoryStatus);


}
