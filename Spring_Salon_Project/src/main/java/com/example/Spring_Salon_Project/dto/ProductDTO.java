package com.example.Spring_Salon_Project.dto;

import com.example.Spring_Salon_Project.enumiration.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductDTO {
    private Long productId;
    private String productName;
    private String description;
    private Double price;
    private Integer qtyOnHand;
    private String productImage;
    private Long categoryId;
    private String categoryName;
    private Long supplierId;
    private String supplierName;
    private ProductStatus productStatus;

}
