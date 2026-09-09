package com.example.Spring_Salon_Project.dto;

import com.example.Spring_Salon_Project.enumiration.CustomerStatus;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomerDTO {
    private Long customerId;
    private String customerName;
    private String phone;
    private CustomerStatus customerStatus;

    private Long userId;
}
