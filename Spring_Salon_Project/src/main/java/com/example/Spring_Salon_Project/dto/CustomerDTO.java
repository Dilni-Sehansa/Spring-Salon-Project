package com.example.Spring_Salon_Project.dto;

import com.example.Spring_Salon_Project.enumiration.CustomerStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomerDTO {
    private Long customerId;

    @NotBlank(message = "Customer name is required")
    @Size(min = 2, max = 100)
    private String customerName;

    @NotBlank
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone must be 10 digits")
    private String phone;
    private CustomerStatus customerStatus;

    private Long userId;
}
