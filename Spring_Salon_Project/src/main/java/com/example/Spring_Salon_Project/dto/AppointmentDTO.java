package com.example.Spring_Salon_Project.dto;

import com.example.Spring_Salon_Project.enumiration.AppointmentStatus;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "Customer ID is required")
    private Long customerId;
    private String customerName;

    @NotNull(message = "Appointment date is required")
    private LocalDate appointmentDate;

    @NotNull(message = "Appointment time is required")
    private LocalTime appointmentTime;
    private AppointmentStatus appointmentStatus;
    private Double totalAmount;

    @NotNull(message = "At least one service is required")
    private List<Long> serviceIds;
}
