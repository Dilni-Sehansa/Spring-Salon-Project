package com.example.Spring_Salon_Project.service.impl;

import com.example.Spring_Salon_Project.dto.PaymentDTO;
import com.example.Spring_Salon_Project.entity.Appointment;
import com.example.Spring_Salon_Project.entity.Payment;
import com.example.Spring_Salon_Project.enumiration.AppointmentStatus;
import com.example.Spring_Salon_Project.enumiration.PaymentStatus;
import com.example.Spring_Salon_Project.exception.CustomerException;
import com.example.Spring_Salon_Project.repository.AppointmentRepository;
import com.example.Spring_Salon_Project.repository.PaymentRepository;
import com.example.Spring_Salon_Project.service.PaymentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final AppointmentRepository appointmentRepository;

    @Override
    public PaymentDTO savePayment(PaymentDTO paymentDTO) {

        if (paymentDTO.getAppointmentId() == null) {
            throw new CustomerException(400, "Appointment ID is required for payment!");
        }

        if (paymentDTO.getTotalAmount() == null || paymentDTO.getTotalAmount() <= 0) {
            throw new CustomerException(400, "Valid total amount is required for payment!");
        }

        if (paymentRepository.existsByAppointment_AppointmentId(paymentDTO.getAppointmentId())) {
            throw new CustomerException(400, "Payment has already been made for this Appointment ID: " + paymentDTO.getAppointmentId());
        }

        Optional<Appointment> appointmentOpt = appointmentRepository.findById(paymentDTO.getAppointmentId());
        if (appointmentOpt.isEmpty()) {
            throw new CustomerException(404, "Appointment not found for ID: " + paymentDTO.getAppointmentId());
        }

        Appointment appointment = appointmentOpt.get();
        appointment.setAppointmentStatus(AppointmentStatus.COMPLETED);
        appointmentRepository.save(appointment);

        Payment payment = new Payment();
        payment.setAppointment(appointment);
        payment.setTotalAmount(paymentDTO.getTotalAmount());

        double discount = (paymentDTO.getDiscount() != null) ? paymentDTO.getDiscount() : 0.0;
        payment.setDiscount(discount);

        double finalAmount = paymentDTO.getTotalAmount() - discount;
        payment.setFinalAmount(finalAmount);

        double amountPaid = (paymentDTO.getAmountPaid() != null) ? paymentDTO.getAmountPaid() : finalAmount;
        if (amountPaid < finalAmount) {
            throw new CustomerException(400, "Amount paid cannot be less than final amount!");
        }
        double changeAmount = amountPaid - finalAmount;

        payment.setAmountPaid(amountPaid);
        payment.setChangeAmount(changeAmount);

        payment.setPaymentMethod(paymentDTO.getPaymentMethod());
        payment.setPaymentStatus(paymentDTO.getPaymentStatus() != null ? paymentDTO.getPaymentStatus() : PaymentStatus.COMPLETED);
        payment.setPaymentDate(paymentDTO.getPaymentDate() != null ? paymentDTO.getPaymentDate() : LocalDateTime.now());

        Payment savedPayment = paymentRepository.save(payment);

        return new PaymentDTO(
                savedPayment.getPaymentId(),
                savedPayment.getAppointment().getAppointmentId(),
                savedPayment.getTotalAmount(),
                savedPayment.getDiscount(),
                savedPayment.getFinalAmount(),
                savedPayment.getAmountPaid(),
                savedPayment.getChangeAmount(),
                savedPayment.getPaymentMethod(),
                savedPayment.getPaymentStatus(),
                savedPayment.getPaymentDate()
        );
    }

    @Override
    public PaymentDTO getPaymentByAppointmentId(Long appointmentId) {
        Optional<PaymentDTO> paymentDTOOpt = paymentRepository.getPaymentByAppointmentId(appointmentId);
        if (paymentDTOOpt.isEmpty()) {
            throw new CustomerException(404, "Payment record not found for Appointment Id: " + appointmentId);
        }

        return paymentDTOOpt.get();
    }

    @Override
    public List<PaymentDTO> getAllPayments() {
        return paymentRepository.getAllPayment();
    }

    @Override
    public PaymentDTO getPaymentById(Long paymentId) {
        Optional<Payment> paymentOpt = paymentRepository.findById(paymentId);
        if (paymentOpt.isEmpty()) {
            throw new CustomerException(404, "Payment not found for ID: " + paymentId);
        }

        Payment payment = paymentOpt.get();

        return new PaymentDTO(
                payment.getPaymentId(),
                payment.getAppointment().getAppointmentId(),
                payment.getTotalAmount(),
                payment.getDiscount(),
                payment.getFinalAmount(),
                payment.getAmountPaid(),
                payment.getChangeAmount(),
                payment.getPaymentMethod(),
                payment.getPaymentStatus(),
                payment.getPaymentDate()
        );
    }
}