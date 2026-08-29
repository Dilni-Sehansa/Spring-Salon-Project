package com.example.Spring_Salon_Project.dto;

import com.example.Spring_Salon_Project.enumiration.ServiceStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SaloonServiceDTO {
    private Long serviceId;
    private String serviceName;
    private String description;
    private Double price;
    private Integer durationMinutes;
    private Long categoryId;
    private String categoryName;
    private ServiceStatus serviceStatus;
}
