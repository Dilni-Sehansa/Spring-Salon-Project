package com.example.Spring_Salon_Project.service;

import com.example.Spring_Salon_Project.dto.AppointmentDetailDTO;
import java.util.List;

public interface AppointmentDetailService {
    List<AppointmentDetailDTO> getDetailsByAppointmentId(Long appointmentId);
    AppointmentDetailDTO getAppointmentDetailById(Long id);
    void deleteAppointmentDetail(Long appointmentServiceId);
    List<AppointmentDetailDTO> getAppointmentDetailsByPhoneAndCustomerName(String phone, String customerName);
}