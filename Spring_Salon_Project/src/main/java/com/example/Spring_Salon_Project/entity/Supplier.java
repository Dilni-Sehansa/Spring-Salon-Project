package com.example.Spring_Salon_Project.entity;

import com.example.Spring_Salon_Project.enumiration.SupplierStatus;
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
@Table(name = "suppliers")
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long supplierId;

    @Column(nullable = false)
    private String supplierName;

    @Column(nullable = false)
    private String contactPerson;

    @Column(nullable = false)
    private String phone;

    private String email;

    private String address;

}
