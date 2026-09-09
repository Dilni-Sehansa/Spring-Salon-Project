package com.example.Spring_Salon_Project.repository;

import com.example.Spring_Salon_Project.dto.PaymentDTO;
import com.example.Spring_Salon_Project.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    boolean existsByAppointment_AppointmentId(Long appointmentId);

    @Query("""
         SELECT NEW com.example.Spring_Salon_Project.dto.PaymentDTO(
              p.paymentId,
              p.appointment.appointmentId,
              p.totalAmount,
              p.discount,
              p.finalAmount,
              p.amountPaid,
              p.changeAmount,
              p.paymentMethod,
              p.paymentStatus,
              p.paymentDate
         )
         FROM Payment p WHERE p.appointment.appointmentId = :appointmentId
         """)
    Optional<PaymentDTO> getPaymentByAppointmentId(@Param("appointmentId") Long appointmentId);

    @Query("""
      SELECT NEW com.example.Spring_Salon_Project.dto.PaymentDTO(
          p.paymentId,
          a.appointmentId,
          p.totalAmount,
          p.discount,
          p.finalAmount,
          p.amountPaid,
          p.changeAmount,
          p.paymentMethod,
          p.paymentStatus,
          p.paymentDate
      )
      FROM Payment p LEFT JOIN p.appointment a ORDER BY p.paymentId DESC
      """)
    List<PaymentDTO> getAllPayment();
}