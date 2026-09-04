package com.example.Spring_Salon_Project.repository;

import com.example.Spring_Salon_Project.dto.AppointmentDTO;
import com.example.Spring_Salon_Project.entity.Appointment;
import com.example.Spring_Salon_Project.enumiration.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("""
        SELECT new com.example.Spring_Salon_Project.dto.AppointmentDTO(
            a.appointmentId,
            c.customerId,
            c.customerName,
            a.appointmentDate,
            a.appointmentTime,
            a.appointmentStatus,
            a.totalAmount,
            null
        )
        FROM Appointment a LEFT JOIN a.customer c ORDER BY a.appointmentId DESC
    """)
    List<AppointmentDTO> getAllAppointments();

    @Query("""
        SELECT new com.example.Spring_Salon_Project.dto.AppointmentDTO(
            a.appointmentId,
            c.customerId,
            c.customerName,
            a.appointmentDate,
            a.appointmentTime,
            a.appointmentStatus,
            a.totalAmount,
            null
        )
        FROM Appointment a LEFT JOIN a.customer c WHERE a.appointmentId = :appointmentId
    """)
    AppointmentDTO selectAppointment(@Param("appointmentId") Long appointmentId);

    @Query("""
        SELECT new com.example.Spring_Salon_Project.dto.AppointmentDTO(
            a.appointmentId,
            c.customerId,
            c.customerName,
            a.appointmentDate,
            a.appointmentTime,
            a.appointmentStatus,
            a.totalAmount,
            null
        )
        FROM Appointment a LEFT JOIN a.customer c WHERE c.customerId = :customerId ORDER BY a.appointmentId DESC
    """)
    List<AppointmentDTO> getAppointmentsByCustomerId(@Param("customerId") Long customerId);

    @Query("""
        SELECT new com.example.Spring_Salon_Project.dto.AppointmentDTO(
            a.appointmentId,
            c.customerId,
            c.customerName,
            a.appointmentDate,
            a.appointmentTime,
            a.appointmentStatus,
            a.totalAmount,
            null
        )
        FROM Appointment a LEFT JOIN a.customer c WHERE a.appointmentDate = :date ORDER BY a.appointmentId DESC
    """)
    List<AppointmentDTO> getAppointmentsByDate(@Param("date") LocalDate date);

    @Query("""
        SELECT new com.example.Spring_Salon_Project.dto.AppointmentDTO(
            a.appointmentId,
            c.customerId,
            c.customerName,
            a.appointmentDate,
            a.appointmentTime,
            a.appointmentStatus,
            a.totalAmount,
            null
        )
        FROM Appointment a LEFT JOIN a.customer c WHERE a.appointmentStatus = :status ORDER BY a.appointmentId DESC
    """)
    List<AppointmentDTO> getAppointmentsByStatus(@Param("status") AppointmentStatus status);
}