package com.example.Spring_Salon_Project.controller;

import com.example.Spring_Salon_Project.dto.CommonResponse;
import com.example.Spring_Salon_Project.dto.CustomerDTO;
import com.example.Spring_Salon_Project.dto.SupplierDTO;
import com.example.Spring_Salon_Project.enumiration.SupplierStatus;
import com.example.Spring_Salon_Project.security.JwtUtil;
import com.example.Spring_Salon_Project.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "v1/supplier")
@RequiredArgsConstructor
public class SupplierController {
    private final SupplierService supplierService;
    private final JwtUtil jwtUtil;

    @PostMapping(value = "/supplier-saved", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse saveSupplier(@RequestBody SupplierDTO supplierDTO){
        SupplierDTO saveSupplier = supplierService.saveSupplier(supplierDTO);
        return new CommonResponse(0,saveSupplier,"Supplier Saved Successfully");
    }

    @GetMapping(value = "/supplier", produces = MediaType.APPLICATION_JSON_VALUE)
        public CommonResponse getAllSuppliers(){
        List<SupplierDTO> getSupplier = supplierService.getAllSuppliers();
        return new CommonResponse(0,getSupplier,"Supplier Loaded Successfully");
    }

    @GetMapping(value = "/{supplierId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getSupplierById(@PathVariable Long supplierId) {
        SupplierDTO supplier = supplierService.getSupplierById(supplierId);
        return new CommonResponse(0, supplier, "Supplier Fetched Successfully");
    }

    @GetMapping(value = "/status/{status}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getSuppliersByStatus(@PathVariable String status) {
        List<SupplierDTO> suppliers = supplierService.getSupplierStatus(status);
        return new CommonResponse(0, suppliers, "Suppliers Loaded Successfully");
    }

    @GetMapping(value = "/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse filterSuppliers(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) SupplierStatus status) {
        List<SupplierDTO> suppliers = supplierService.filterSuppliers(keyword, status);
        return new CommonResponse(0, suppliers, "Suppliers Filtered Successfully");
    }

    @DeleteMapping(value = "/{supplierId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse deleteSupplier(@PathVariable long supplierId){
        supplierService.deleteSupplier(supplierId);
        return new CommonResponse(0,"Supplier Deleted Successfully");
    }

    @PutMapping(value = "/update-supplier",produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updateSupplier(@RequestBody SupplierDTO supplierDTO){
        supplierService.updateSupplier(supplierDTO);
        return new CommonResponse(0,"Supplier Updated Successfully");
    }

    @PatchMapping(value = "/update-status/{supplierId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updateSupplierStatus(
            @PathVariable Long supplierId,
            @RequestParam SupplierStatus status) {
        supplierService.changeStatus(supplierId, status);
        return new CommonResponse(0, "Supplier Status Updated Successfully");
    }
}
