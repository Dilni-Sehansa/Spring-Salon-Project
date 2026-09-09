package com.example.Spring_Salon_Project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AppointmentDetailDTO {
    private Long appointmentServiceId;
    private Double price;
    private Long appointmentId;
    private Long serviceId;
    private String serviceName;
    private Boolean deleted;
}
