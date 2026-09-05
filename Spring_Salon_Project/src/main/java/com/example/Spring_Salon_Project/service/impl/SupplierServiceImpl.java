package com.example.Spring_Salon_Project.service.impl;

import com.example.Spring_Salon_Project.dto.SupplierDTO;
import com.example.Spring_Salon_Project.entity.Supplier;
import com.example.Spring_Salon_Project.enumiration.SupplierStatus;
import com.example.Spring_Salon_Project.exception.CustomerException;
import com.example.Spring_Salon_Project.repository.SupplierRepository;
import com.example.Spring_Salon_Project.service.SupplierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;

    @Override
    public SupplierDTO saveSupplier(SupplierDTO supplierDTO) {
        log.info("Execute method saveSupplier");
        try {
            if (supplierRepository.existsByPhone(supplierDTO.getPhone())) {
                throw new CustomerException(400, "Phone already exists");
            }

            if (supplierDTO.getEmail() != null && !supplierDTO.getEmail().trim().isEmpty()
                    && supplierRepository.existsByEmail(supplierDTO.getEmail())) {
                throw new CustomerException(400, "Email already exists");
            }

            Supplier supplier = new Supplier();
            supplier.setSupplierName(supplierDTO.getSupplierName());
            supplier.setContactPerson(supplierDTO.getContactPerson());
            supplier.setPhone(supplierDTO.getPhone());
            supplier.setEmail(supplierDTO.getEmail());
            supplier.setAddress(supplierDTO.getAddress());
            supplier.setSupplierStatus(supplierDTO.getSupplierStatus() != null ? supplierDTO.getSupplierStatus() : SupplierStatus.ACTIVE);

            Supplier savedSupplier = supplierRepository.save(supplier);
            log.info("Supplier saved successfully");

            return new SupplierDTO(
                    savedSupplier.getSupplierId(),
                    savedSupplier.getSupplierName(),
                    savedSupplier.getContactPerson(),
                    savedSupplier.getPhone(),
                    savedSupplier.getEmail(),
                    savedSupplier.getAddress(),
                    savedSupplier.getSupplierStatus()
            );
        } catch (Exception e) {
            log.error("Error saving Supplier: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public void updateSupplier(SupplierDTO supplierDTO) {
        log.info("Execute method updateSupplier for ID: {}", supplierDTO.getSupplierId());

        Optional<Supplier> optionalSupplier = supplierRepository.findById(supplierDTO.getSupplierId());

        if (optionalSupplier.isEmpty()) {
            throw new CustomerException(404, "Supplier not found");
        }

        Supplier supplier = optionalSupplier.get();

        if (supplierDTO.getPhone() != null && !supplierDTO.getPhone().trim().isEmpty()) {
            if (!supplier.getPhone().equals(supplierDTO.getPhone()) && supplierRepository.existsByPhone(supplierDTO.getPhone())) {
                throw new CustomerException(400, "Phone already exists");
            }
            supplier.setPhone(supplierDTO.getPhone());
        }

        if (supplierDTO.getEmail() != null && !supplierDTO.getEmail().trim().isEmpty()) {
            if (!supplierDTO.getEmail().equals(supplier.getEmail()) && supplierRepository.existsByEmail(supplierDTO.getEmail())) {
                throw new CustomerException(400, "Email already exists");
            }
            supplier.setEmail(supplierDTO.getEmail());
        }

        supplier.setSupplierName(supplierDTO.getSupplierName());

        if (supplierDTO.getContactPerson() != null && !supplierDTO.getContactPerson().trim().isEmpty()) {
            supplier.setContactPerson(supplierDTO.getContactPerson());
        }

        if (supplierDTO.getAddress() != null && !supplierDTO.getAddress().trim().isEmpty()) {
            supplier.setAddress(supplierDTO.getAddress());
        }

        if (supplierDTO.getSupplierStatus() != null) {
            supplier.setSupplierStatus(supplierDTO.getSupplierStatus());
        }

        supplierRepository.save(supplier);
        log.info("Supplier updated successfully");
    }

    @Override
    public void deleteSupplier(Long supplierId) {
        log.info("Execute method deleteSupplier() supplierId {}", supplierId);

        try {
            Optional<Supplier> optionalSupplier = supplierRepository.findById(supplierId);

            if (optionalSupplier.isEmpty() || optionalSupplier.get().getSupplierStatus() == SupplierStatus.INACTIVE) {
                throw new CustomerException(404, "Supplier not found");
            }

            Supplier supplier = optionalSupplier.get();
            supplier.setSupplierStatus(SupplierStatus.INACTIVE);
            supplierRepository.save(supplier);
            log.info("Supplier deleted successfully");
        } catch (Exception e) {
            log.error("Error deleting Supplier: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public SupplierDTO getSupplierById(Long supplierId) {
        log.info("Execute method getSupplierById for ID: {}", supplierId);
        SupplierDTO supplierDTO = supplierRepository.getSupplierById(supplierId);
        if (supplierDTO == null) {
            throw new CustomerException(404, "Supplier not found with ID: " + supplierId);
        }
        return supplierDTO;
    }

    @Override
    public List<SupplierDTO> getAllSuppliers() {
        return supplierRepository.getAllSuppliers();
    }

    @Override
    public List<SupplierDTO> getSupplierStatus(String supplierStatus) {
        log.info("Execute method getSupplierStatus for status: {}", supplierStatus);
        try {
            SupplierStatus supplierStatus1 = SupplierStatus.valueOf(supplierStatus.toUpperCase());
            return supplierRepository.getSuppliersByStatus(supplierStatus1);
        } catch (IllegalArgumentException e) {
            throw new CustomerException(400, "Invalid status: " + supplierStatus);
        }
    }

    @Override
    public void changeStatus(Long supplierId, SupplierStatus status) {
        log.info("Execute method changeStatus for ID: {} to Status: {}", supplierId, status);

        Optional<Supplier> optionalSupplier = supplierRepository.findById(supplierId);

        if (optionalSupplier.isEmpty()) {
            throw new CustomerException(404, "Supplier not found");
        }

        Supplier supplier = optionalSupplier.get();
        supplier.setSupplierStatus(status);
        supplierRepository.save(supplier);
        log.info("Supplier status updated successfully");
    }

    @Override
    public List<SupplierDTO> filterSuppliers(String keyword, SupplierStatus status) {
        log.info("Execute method filterSuppliers with keyword: {} and status: {}", keyword, status);
        String formattedKeyword = (keyword != null) ? keyword.trim() : "";
        return supplierRepository.filterSuppliers(formattedKeyword, status);
    }
}