package com.example.Spring_Salon_Project.dto;

import com.example.Spring_Salon_Project.enumiration.Status;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StaffServiceDTO {
    private Long staffServiceId;

    @NotNull(message = "Staff ID is required")
    private Long staffId;
    private String staffName;

    @NotNull(message = "Service ID is required")
    private Long serviceId;
    private String serviceName;
    private Status staffServiceStatus;


}
