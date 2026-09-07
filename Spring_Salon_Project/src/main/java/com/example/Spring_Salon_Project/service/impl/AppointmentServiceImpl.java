package com.example.Spring_Salon_Project.service.impl;

import com.example.Spring_Salon_Project.dto.AppointmentDTO;
import com.example.Spring_Salon_Project.entity.*;
import com.example.Spring_Salon_Project.enumiration.AppointmentStatus;
import com.example.Spring_Salon_Project.exception.CustomerException;
import com.example.Spring_Salon_Project.repository.AppointmentDetailRepository;
import com.example.Spring_Salon_Project.repository.AppointmentRepository;
import com.example.Spring_Salon_Project.repository.SaloonServiceRepository;
import com.example.Spring_Salon_Project.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentDetailRepository appointmentDetailRepository;
    private final SaloonServiceRepository saloonServiceRepository;

    @Override
    public AppointmentDTO saveAppointment(AppointmentDTO appointmentDTO) {
        log.info("Execute method saveAppointment");

        try {
            Appointment appointment = new Appointment();
            appointment.setAppointmentDate(appointmentDTO.getAppointmentDate());
            appointment.setAppointmentTime(appointmentDTO.getAppointmentTime());
            appointment.setTotalAmount(appointmentDTO.getTotalAmount());

            if (appointmentDTO.getAppointmentStatus() != null) {
                appointment.setAppointmentStatus(appointmentDTO.getAppointmentStatus());
            } else {
                appointment.setAppointmentStatus(AppointmentStatus.PENDING);
            }

            if (appointmentDTO.getCustomerId() != null) {
                Customer customer = new Customer();
                customer.setCustomerId(appointmentDTO.getCustomerId());
                appointment.setCustomer(customer);
            }

            Appointment save = appointmentRepository.save(appointment);
            log.info("Appointment saved successfully");

            if (appointmentDTO.getServiceIds() != null && !appointmentDTO.getServiceIds().isEmpty()) {
                for (Long serviceId : appointmentDTO.getServiceIds()) {
                    Optional<SaloonService> optionalService = saloonServiceRepository.findById(serviceId);

                    if (optionalService.isPresent()) {
                        SaloonService service = optionalService.get();

                        if (service.getServiceStatus() != null &&
                                service.getServiceStatus().name().equalsIgnoreCase("INACTIVE")) {
                            log.warn("Skipping INACTIVE service ID: {}", serviceId);
                            continue;
                        }

                        AppointmentDetail detail = new AppointmentDetail();
                        detail.setAppointment(save);
                        detail.setService(service);
                        detail.setPrice(service.getPrice());

                        appointmentDetailRepository.save(detail);
                        log.info("Saved AppointmentDetail for serviceId: {}", serviceId);
                    } else {
                        log.warn("Service not found for ID: {}", serviceId);
                    }
                }
            }

            Long savedCustomerId = (save.getCustomer() != null) ? save.getCustomer().getCustomerId() : null;
            String savedCustomerName = (save.getCustomer() != null) ? save.getCustomer().getCustomerName() : null;

            return new AppointmentDTO(
                    save.getAppointmentId(),
                    savedCustomerId,
                    savedCustomerName,
                    save.getAppointmentDate(),
                    save.getAppointmentTime(),
                    save.getAppointmentStatus(),
                    save.getTotalAmount(),
                    appointmentDTO.getServiceIds()
            );

        } catch (Exception e) {
            log.error("Error saving appointment: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public void updateAppointment(AppointmentDTO appointmentDTO) {
        log.info("Execute method updateAppointment for ID: {}", appointmentDTO.getAppointmentId());

        Optional<Appointment> optionalAppointment = appointmentRepository.findById(appointmentDTO.getAppointmentId());

        if (optionalAppointment.isEmpty()) {
            throw new CustomerException(404, "Appointment not found");
        }

        try {
            Appointment appointment = optionalAppointment.get();
            appointment.setAppointmentDate(appointmentDTO.getAppointmentDate());
            appointment.setAppointmentTime(appointmentDTO.getAppointmentTime());

            if (appointmentDTO.getTotalAmount() != null) {
                appointment.setTotalAmount(appointmentDTO.getTotalAmount());
            }

            if (appointmentDTO.getAppointmentStatus() != null) {
                appointment.setAppointmentStatus(appointmentDTO.getAppointmentStatus());
            }

            if (appointmentDTO.getCustomerId() != null) {
                Customer customer = new Customer();
                customer.setCustomerId(appointmentDTO.getCustomerId());
                appointment.setCustomer(customer);
            }

            appointmentRepository.save(appointment);
            log.info("Appointment updated successfully");

            if (appointmentDTO.getServiceIds() != null) {

                List<AppointmentDetail> existingDetails = appointmentDetailRepository
                        .findByAppointment_AppointmentId(appointment.getAppointmentId());

                if (existingDetails != null && !existingDetails.isEmpty()) {
                    appointmentDetailRepository.deleteAll(existingDetails);
                }

                for (Long serviceId : appointmentDTO.getServiceIds()) {
                    Optional<SaloonService> optionalService = saloonServiceRepository.findById(serviceId);

                    if (optionalService.isPresent()) {
                        SaloonService service = optionalService.get();

                        if (service.getServiceStatus() != null &&
                                service.getServiceStatus().name().equalsIgnoreCase("INACTIVE")) {
                            continue;
                        }

                        AppointmentDetail detail = new AppointmentDetail();
                        detail.setAppointment(appointment);
                        detail.setService(service);
                        detail.setPrice(service.getPrice());

                        appointmentDetailRepository.save(detail);
                    }
                }
                log.info("Appointment details updated successfully");
            }

        } catch (Exception e) {
            log.error("Error updating appointment: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public void deleteAppointment(long appointmentId) {
        log.info("Execute method deleteAppointment() appointmentId: {}", appointmentId);

        try {
            Optional<Appointment> optionalAppointment = appointmentRepository.findById(appointmentId);

            if (optionalAppointment.isEmpty() || optionalAppointment.get().getAppointmentStatus() == AppointmentStatus.CANCELLED) {
                throw new CustomerException(404, "Appointment not found or already cancelled: " + appointmentId);
            }

            Appointment appointment = optionalAppointment.get();
            appointment.setAppointmentStatus(AppointmentStatus.CANCELLED);
            appointmentRepository.save(appointment);

            log.info("Appointment deleted successfully");

        } catch (Exception e) {
            log.error("Error deleting appointment");
            throw e;
        }
    }

    @Override
    public AppointmentDTO selectAppointment(long appointmentId) {
        log.info("Execute method selectAppointment for ID: {}", appointmentId);

        AppointmentDTO appointmentDTO = appointmentRepository.selectAppointment(appointmentId);
        if (appointmentDTO == null) {
            throw new CustomerException(404, "Appointment not found for ID: " + appointmentId);
        }
        return appointmentDTO;
    }

    @Override
    public List<AppointmentDTO> getAllAppointments() {
        log.info("Execute method getAllAppointments");
        return appointmentRepository.getAllAppointments();
    }

    @Override
    public List<AppointmentDTO> getAppointmentsByCustomerId(Long customerId) {
        log.info("Execute method getAppointmentsByCustomerId for customerId: {}", customerId);
        return appointmentRepository.getAppointmentsByCustomerId(customerId);
    }

    @Override
    public List<AppointmentDTO> getAppointmentsByDate(LocalDate date) {
        log.info("Execute method getAppointmentsByDate for date: {}", date);
        return appointmentRepository.getAppointmentsByDate(date);
    }

    @Override
    public List<AppointmentDTO> getAppointmentsByStatus(String status) {
        log.info("Execute method getAppointmentsByStatus for status: {}", status);
        try {
            AppointmentStatus appointmentStatus = AppointmentStatus.valueOf(status.toUpperCase());
            return appointmentRepository.getAppointmentsByStatus(appointmentStatus);
        } catch (IllegalArgumentException e) {
            throw new CustomerException(400, "Invalid status: " + status);
        }
    }

    @Override
    public void updateAppointmentStatus(Long appointmentId, AppointmentStatus status) {
        log.info("Execute method updateAppointmentStatus for ID: {} to Status: {}", appointmentId, status);

        Optional<Appointment> optionalAppointment = appointmentRepository.findById(appointmentId);

        if (optionalAppointment.isEmpty()) {
            throw new CustomerException(404, "Appointment not found");
        }
            Appointment appointment = optionalAppointment.get();
            appointment.setAppointmentStatus(status);
            appointmentRepository.save(appointment);
            log.info("Appointment status updated successfully");

    }

}