package com.example.Spring_Salon_Project.service;

import com.example.Spring_Salon_Project.dto.AppointmentDTO;
import com.example.Spring_Salon_Project.dto.SupplierDTO;
import com.example.Spring_Salon_Project.enumiration.SupplierStatus;

import java.util.List;

public interface SupplierService {
    SupplierDTO saveSupplier(SupplierDTO supplierDTO);
    void updateSupplier(SupplierDTO supplierDTO);
    void deleteSupplier(Long supplierId);
    SupplierDTO getSupplierById(Long supplierId);
    List<SupplierDTO> getAllSuppliers();
    List<SupplierDTO> getSupplierStatus(String supplierStatus);
    void changeStatus(Long supplierId, SupplierStatus status);
    List<SupplierDTO> filterSuppliers(String keyword, SupplierStatus status);
}
