package com.example.Spring_Salon_Project.service.impl;

import com.example.Spring_Salon_Project.dto.AppointmentDetailDTO;
import com.example.Spring_Salon_Project.dto.AuditLogDTO;
import com.example.Spring_Salon_Project.entity.AppointmentDetail;
import com.example.Spring_Salon_Project.exception.CustomerException;
import com.example.Spring_Salon_Project.repository.AppointmentDetailRepository;
import com.example.Spring_Salon_Project.service.AppointmentDetailService;
import com.example.Spring_Salon_Project.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentDetailServiceImpl implements AppointmentDetailService {

    private final AppointmentDetailRepository appointmentDetailRepository;
    private final AuditLogService auditLogService;

    @Override
    public List<AppointmentDetailDTO> getDetailsByAppointmentId(Long appointmentId) {
        log.info("Execute method getDetailsByAppointmentId for appointmentId: {}", appointmentId);
        try {
            return appointmentDetailRepository.getDetailsByAppointmentId(appointmentId);
        } catch (Exception e) {
            log.error("Error fetching details for appointmentId {}: {}", appointmentId, e.getMessage());
            throw e;
        }
    }

    @Override
    public AppointmentDetailDTO getAppointmentDetailById(Long id) {
        log.info("Execute method getAppointmentDetailById for ID: {}", id);
        try {
            AppointmentDetailDTO detailDTO = appointmentDetailRepository.getAppointmentDetailById(id);
            if (detailDTO == null) {
                throw new CustomerException(404, "Appointment detail not found for ID: " + id);
            }
            return detailDTO;
        } catch (Exception e) {
            log.error("Error fetching appointment detail for ID {}: {}", id, e.getMessage());
            throw e;
        }
    }

    @Override
    public void deleteAppointmentDetail(Long appointmentServiceId) {
        log.info("Execute method deleteAppointmentDetail() appointmentServiceId: {}", appointmentServiceId);

        try {
            Optional<AppointmentDetail> optionalAppointmentDetail = appointmentDetailRepository.findById(appointmentServiceId);

            if (optionalAppointmentDetail.isEmpty()) {
                throw new CustomerException(404, "Appointment detail not found for ID: " + appointmentServiceId);
            }

            AppointmentDetail detail = optionalAppointmentDetail.get();

            if (Boolean.TRUE.equals(detail.getDeleted())) {
                throw new CustomerException(400, "Appointment detail already deleted");
            }

            detail.setDeleted(true);
            appointmentDetailRepository.save(detail);

            log.info("Appointment Detail soft-deleted successfully");

            AuditLogDTO logDTO = new AuditLogDTO();
            logDTO.setAction("DELETE");
            logDTO.setEntityName("APPOINTMENT_DETAIL");
            logDTO.setEntityId(detail.getAppointmentServiceId());
            logDTO.setPerformedBy("admin");
            logDTO.setDetails("Appointment detail soft-deleted. ID: " + detail.getAppointmentServiceId());
            auditLogService.saveAuditLog(logDTO);

        }catch (CustomerException e){
            throw e;
        } catch (Exception e) {
            log.error("Error deleting appointment detail for appointmentServiceId {}: {}", appointmentServiceId, e.getMessage());
            throw e;
        }
    }

    @Override
    public List<AppointmentDetailDTO> getAppointmentDetailsByPhoneAndCustomerName(String phone, String customerName) {
        log.info("Execute method getAppointmentDetailsByPhoneAndCustomerName - phone: {}, customerName: {}", phone, customerName);
        try {
            return appointmentDetailRepository.getAppointmentDetailsByPhoneAndCustomerName(phone, customerName);
        } catch (Exception e) {
            log.error("Error fetching appointment details by phone and customerName: {}", e.getMessage());
            throw e;
        }
    }
}