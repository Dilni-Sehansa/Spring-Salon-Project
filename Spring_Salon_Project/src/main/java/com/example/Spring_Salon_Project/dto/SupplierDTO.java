package com.example.Spring_Salon_Project.dto;

import com.example.Spring_Salon_Project.enumiration.SupplierStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SupplierDTO {
    private Long supplierId;
    private String supplierName;
    private String contactPerson;
    private String phone;
    private String email;
    private String address;
    private SupplierStatus supplierStatus;
}
