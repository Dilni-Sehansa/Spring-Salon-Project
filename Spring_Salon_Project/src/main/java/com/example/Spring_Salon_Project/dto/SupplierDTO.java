package com.example.Spring_Salon_Project.dto;

import com.example.Spring_Salon_Project.enumiration.SupplierStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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

    @NotBlank(message = "Supplier name is required")
    private String supplierName;
    private String contactPerson;

    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone must be 10 digits")
    private String phone;

    @Email(message = "Invalid email format")
    private String email;
    private String address;
    private SupplierStatus supplierStatus;
}
