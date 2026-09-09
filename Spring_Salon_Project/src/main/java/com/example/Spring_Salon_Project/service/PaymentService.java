package com.example.Spring_Salon_Project.service;

import com.example.Spring_Salon_Project.dto.PaymentDTO;

import java.util.List;

public interface PaymentService {
    PaymentDTO savePayment(PaymentDTO paymentDTO);
    PaymentDTO getPaymentByAppointmentId(Long appointmentId);
    List<PaymentDTO> getAllPayments();
    PaymentDTO getPaymentById(Long paymentId);
}
