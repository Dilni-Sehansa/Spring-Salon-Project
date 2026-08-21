package com.example.Spring_Salon_Project.dto;

import com.example.Spring_Salon_Project.enumiration.CustomerStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomerDTO {
    private Long customerId;
    private String firstName;
    private String lastName;
    private String phone;
    private CustomerStatus customerStatus;

    private Long userId;
}
