package com.example.Spring_Salon_Project.service;

import com.example.Spring_Salon_Project.dto.AppointmentDTO;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentService {
    AppointmentDTO saveAppointment(AppointmentDTO appointmentDTO);
    void updateAppointment(AppointmentDTO appointmentDTO);
    void deleteAppointment(long appointmentId);
    AppointmentDTO selectAppointment(long appointmentId);
    List<AppointmentDTO> getAllAppointments();
    List<AppointmentDTO> getAppointmentsByCustomerId(Long customerId);
    List<AppointmentDTO> getAppointmentsByDate(LocalDate date);
    List<AppointmentDTO> getAppointmentsByStatus(String status);
    void updateAppointmentStatus(Long appointmentId, String status);

}
