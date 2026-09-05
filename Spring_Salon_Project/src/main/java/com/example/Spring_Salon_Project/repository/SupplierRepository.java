package com.example.Spring_Salon_Project.repository;

import com.example.Spring_Salon_Project.dto.SupplierDTO;
import com.example.Spring_Salon_Project.entity.Supplier;
import com.example.Spring_Salon_Project.enumiration.SupplierStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    boolean existsByPhone(String phone);

    boolean existsByEmail(String email);

    @Query("""
        SELECT new com.example.Spring_Salon_Project.dto.SupplierDTO(
            s.supplierId,
            s.supplierName,
            s.contactPerson,
            s.phone,
            s.email,
            s.address,
            s.supplierStatus
        )
        FROM Supplier s
        ORDER BY s.supplierId DESC
    """)
    List<SupplierDTO> getAllSuppliers();

    @Query("""
        SELECT new com.example.Spring_Salon_Project.dto.SupplierDTO(
            s.supplierId,
            s.supplierName,
            s.contactPerson,
            s.phone,
            s.email,
            s.address,
            s.supplierStatus
        )
        FROM Supplier s
        WHERE s.supplierStatus = :status
        ORDER BY s.supplierName ASC
    """)
    List<SupplierDTO> getSuppliersByStatus(@Param("status") SupplierStatus status);

    @Query("""
        SELECT new com.example.Spring_Salon_Project.dto.SupplierDTO(
            s.supplierId,
            s.supplierName,
            s.contactPerson,
            s.phone,
            s.email,
            s.address,
            s.supplierStatus
        )
        FROM Supplier s
        WHERE s.supplierId = :supplierId
    """)
    SupplierDTO getSupplierById(@Param("supplierId") Long supplierId);

    @Query("""
    SELECT new com.example.Spring_Salon_Project.dto.SupplierDTO(
        s.supplierId,
        s.supplierName,
        s.contactPerson,
        s.phone,
        s.email,
        s.address,
        s.supplierStatus
    )
    FROM Supplier s
    WHERE (:status IS NULL OR s.supplierStatus = :status)
    AND (
        :keyword IS NULL OR :keyword = '' OR 
        LOWER(s.supplierName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR 
        LOWER(s.contactPerson) LIKE LOWER(CONCAT('%', :keyword, '%')) OR 
        s.phone LIKE CONCAT('%', :keyword, '%') OR
        LOWER(s.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
    )
    ORDER BY s.supplierId DESC
""")
    List<SupplierDTO> filterSuppliers(@Param("keyword") String keyword, @Param("status") SupplierStatus status);
}
/*
* private Long supplierId;
    private String supplierName;
    private String phone;
    private String email;
    private String address;
    private SupplierStatus supplierStatus;*/