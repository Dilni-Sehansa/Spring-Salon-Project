package com.example.Spring_Salon_Project.repository;

import com.example.Spring_Salon_Project.dto.ProductDTO;
import com.example.Spring_Salon_Project.dto.StaffDTO;
import com.example.Spring_Salon_Project.entity.Product;
import com.example.Spring_Salon_Project.enumiration.ProductStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {

    @Query("""
      SELECT NEW com.example.Spring_Salon_Project.dto.ProductDTO(
          p.productId,
          p.productName,
          p.description,
          p.price,
          p.qtyOnHand,
          p.productImage,
          c.categoryId,
          c.categoryName,
          s.supplierId,
          s.supplierName,
          p.productStatus
      )
      FROM Product p LEFT JOIN p.category c LEFT JOIN p.supplier s ORDER BY p.productId DESC
      """)
    List<ProductDTO> getAllProducts();

    @Query("""
      SELECT NEW com.example.Spring_Salon_Project.dto.ProductDTO(
          p.productId,
          p.productName,
          p.description,
          p.price,
          p.qtyOnHand,
          p.productImage,
          c.categoryId,
          c.categoryName,
          s.supplierId,
          s.supplierName,
          p.productStatus
      )
      FROM Product p LEFT JOIN p.category c LEFT JOIN p.supplier s WHERE p.productStatus = :productStatus ORDER BY p.productId DESC
      """)
    List<ProductDTO> getProductsByStatus(@Param("productStatus") ProductStatus productStatus);

    @Query("""
      SELECT NEW com.example.Spring_Salon_Project.dto.ProductDTO(
          p.productId,
          p.productName,
          p.description,
          p.price,
          p.qtyOnHand,
          p.productImage,
          c.categoryId,
          c.categoryName,
          s.supplierId,
          s.supplierName,
          p.productStatus
      )
      FROM Product p LEFT JOIN p.category c LEFT JOIN p.supplier s WHERE p.productId = :productId
      """)
    Optional<ProductDTO> selectProduct(@Param("productId") Long productId);

    @Query("""
      SELECT NEW com.example.Spring_Salon_Project.dto.ProductDTO(
          p.productId,
          p.productName,
          p.description,
          p.price,
          p.qtyOnHand,
          p.productImage,
          c.categoryId,
          c.categoryName,
          s.supplierId,
          s.supplierName,
          p.productStatus
      )
      FROM Product p LEFT JOIN p.category c LEFT JOIN p.supplier s WHERE LOWER(p.productName) LIKE LOWER(CONCAT('%', :keyword, '%'))
      """)
    List<ProductDTO> findByProductName(@Param("keyword") String keyword);

    @Query("""
      SELECT NEW com.example.Spring_Salon_Project.dto.ProductDTO(
          p.productId,
          p.productName,
          p.description,
          p.price,
          p.qtyOnHand,
          p.productImage,
          c.categoryId,
          c.categoryName,
          s.supplierId,
          s.supplierName,
          p.productStatus
      )
      FROM Product p LEFT JOIN p.category c LEFT JOIN p.supplier s WHERE c.categoryId = :categoryId
      """)
    List<ProductDTO> getProductsByCategory(@Param("categoryId") Long categoryId);

    @Query("""
      SELECT NEW com.example.Spring_Salon_Project.dto.ProductDTO(
          p.productId,
          p.productName,
          p.description,
          p.price,
          p.qtyOnHand,
          p.productImage,
          c.categoryId,
          c.categoryName,
          s.supplierId,
          s.supplierName,
          p.productStatus
      )
      FROM Product p LEFT JOIN p.category c LEFT JOIN p.supplier s WHERE s.supplierId = :supplierId
      """)
    List<ProductDTO> getProductsBySupplier(@Param("supplierId") Long supplierId);

    @Query("""
      SELECT NEW com.example.Spring_Salon_Project.dto.ProductDTO(
          p.productId,
          p.productName,
          p.description,
          p.price,
          p.qtyOnHand,
          p.productImage,
          c.categoryId,
          c.categoryName,
          s.supplierId,
          s.supplierName,
          p.productStatus
      )
      FROM Product p LEFT JOIN p.category c LEFT JOIN p.supplier s WHERE p.qtyOnHand <= :thresholdQty
      """)
    List<ProductDTO> getLowStockProducts(@Param("thresholdQty") Integer thresholdQty);

    /*
    *  private Long productId;
    private String productName;
    private String description;
    private Double price;
    private Integer qtyOnHand;
    private String productImage;
    private Long categoryId;
    private String categoryName;
    private Long supplierId;
    private String supplierName;
    private ProductStatus productStatus;*/
}
