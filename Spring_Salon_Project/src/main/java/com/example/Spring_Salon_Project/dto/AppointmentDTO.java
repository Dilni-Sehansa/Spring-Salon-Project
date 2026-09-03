package com.example.Spring_Salon_Project.dto;

import com.example.Spring_Salon_Project.enumiration.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AppointmentDTO {
    private Long appointmentId;
    private Long customerId;
    private String customerName;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private AppointmentStatus appointmentStatus;
    private Double totalAmount;
    private List<Long> serviceIds;
}
