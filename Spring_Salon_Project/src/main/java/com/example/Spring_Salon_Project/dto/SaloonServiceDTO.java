package com.example.Spring_Salon_Project.dto;

import com.example.Spring_Salon_Project.enumiration.ServiceStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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

    @NotBlank(message = "Service name is required")
    private String serviceName;
    private String description;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private Double price;

    @NotNull(message = "Duration is required")
    @Positive(message = "Duration must be positive")
    private Integer durationMinutes;

    @NotNull(message = "Category ID is required")
    private Long categoryId;
    private String categoryName;
    private ServiceStatus serviceStatus;
}
