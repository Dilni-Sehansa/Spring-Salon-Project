package com.example.Spring_Salon_Project.dto;

import com.example.Spring_Salon_Project.enumiration.PaymentMethod;
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
    private Long appointmentId;
    private Double totalAmount;
    private Double discount;
    private Double finalAmount;
    private PaymentMethod paymentMethod;
    private LocalDateTime paymentDate;
}
