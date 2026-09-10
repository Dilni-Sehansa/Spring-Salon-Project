package com.example.Spring_Salon_Project.dto;

import com.example.Spring_Salon_Project.enumiration.PaymentMethod;
import com.example.Spring_Salon_Project.enumiration.PaymentStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaymentDTO {
    private Long paymentId;

    @NotNull(message = "Appointment ID is required")
    private Long appointmentId;

    @NotNull(message = "Total amount is required")
    @Positive
    private Double totalAmount;
    private Double discount;
    private Double finalAmount;

    @NotNull(message = "Amount paid is required")
    @Positive
    private Double amountPaid;
    private Double changeAmount;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private LocalDateTime paymentDate;
}
